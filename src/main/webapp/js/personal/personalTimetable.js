$(document).ready(function(){
	// 设置日历面板左侧展示的年、月、日、星期
	function setYearMonthDateDay(selectedDate){
		let $selectedDate = $("#calendar .selected_date");
    	let $selectedDay = $("#calendar .selected_day");
    	let $yyyyMM = $("#calendar .fc-center>h2");
    	let date = "";
    	let day = "";
    	if (selectedDate == null) {
    		// 日期为空时，不显示日期和星期信息
    		$selectedDate.text(date);
	    	$selectedDay.text(day);
		}else{
			let year = selectedDate.getFullYear();
	    	let month = selectedDate.getMonth() + 1;
	    	date = selectedDate.getDate();
	    	switch (selectedDate.getDay()) {
			case 0:
				day = "周日";
				break;
			case 1:
				day = "周一";
				break;
			case 2:
				day = "周二";
				break;
			case 3:
				day = "周三";
				break;
			case 4:
				day = "周四";
				break;
			case 5:
				day = "周五";
				break;
			case 6:
				day = "周六";
				break;
			}
	    	$yyyyMM.text(`${year}年${month}月`);
	    	$selectedDate.text(date);
	    	$selectedDay.text(day);
		}
	}
	// 初始化日历对象
	var calendar = new FullCalendar.Calendar($("#calendar")[0], {
	    plugins: [ "interaction", "dayGrid" ],
	    defaultView: "dayGridMonth",
	    locale: "zh-cn",
	    firstDay: 0,
	    aspectRatio: 2.5,
	    showNonCurrentDates: true,
	    fixedWeekCount: false,
	    header: {
	    	left: "prev",
	    	center: "title",
	    	right: "next"
	    }
	});
	calendar.render();// 页面渲染日历
	var $toolbar = $("#calendar>.fc-toolbar");
	if ($toolbar.children(".custom_container").length == 0) {
		// 初始化日历面板左侧展示的年、月、日、星期
		let html = `
    	<div class="custom_container normal">
    		<div class="date_wrap">
    			<span class="selected_date"></span>
    			<span class="selected_day"></span>
    		</div>
    		<div class="button_wrap">
    			<span>今天</span>
    		</div>
    	</div>
    	<div class="button_wrap"></div>`;
		$toolbar.append(html);
		setYearMonthDateDay(new Date());
	}
	function getDateString(dateObj){
		let year = dateObj.getFullYear();
		let monthTemp = dateObj.getMonth() + 1;
		let month =  monthTemp < 10 ? "0"+ monthTemp : monthTemp;
		let dateTemp = dateObj.getDate();
		let date = dateTemp < 10 ? "0"+ dateTemp : dateTemp;
		return year+"-"+month+"-"+date;
	}
	function getTimestamp(dateStr, timeStr){
		let dateArray = dateStr.split("-");
		for (let i = 0; i < dateArray.length; i++) {
			dateArray[i] = Number(dateArray[i]);
		}
		let timeArray = timeStr.split(":");
		for (let i = 0; i < timeArray.length; i++) {
			timeArray[i] = Number(timeArray[i]);
		}
		let time = new Date(dateArray[0],dateArray[1]-1,dateArray[2],timeArray[0],timeArray[1])
		return time.getTime();
	}
	function renderTimetable(beginStr, endStr, todayStr){
		$.post("personalTimetable/listPersonalTimetables.action",{
			begin : beginStr,
			end : endStr
		},function(map){
			if (map.code == 600) {
				let array = map.data;
				let html ="";
				let showArray = [];
				for (let i = 0; i < array.length; i++) {
					$(`#calendar .fc-bg td.fc-day[data-date="${array[i].chapter_date}"]`)
						.addClass("has_course");
					if (array[i].chapter_date == todayStr) {
						showArray.push(array[i]);
					}
				}
				if (showArray.length == 0) {
					html = "<div class='notice'>暂无课程！</div>";
				} else {
					for (let j = 0; j < showArray.length; j++) {
						html += `
						<div class="timetable_row">
							<div class="timetable_column">
								<div class="course_name">${showArray[j].course_name}</div>
								<div class="chapter_wrap">
									<span class="chapter_number">第<span class="number_val">${showArray[j].chapter_number}</span>讲</span>
									<span class="chapter_name">${showArray[j].chapter_name}</span>
								</div>
							</div>
							<div class="timetable_column">
								<div class="chapter_date">${showArray[j].chapter_date}</div>
								<div class="chapter_time">
									<span class="chapter_begin">${showArray[j].chapter_begin}</span>
									<span>-</span>
									<span class="chapter_end">${showArray[j].chapter_end}</span>
								</div>
							</div>`;
						let begin = getTimestamp(showArray[j].chapter_date,showArray[j].chapter_begin);
						let end = getTimestamp(showArray[j].chapter_date,showArray[j].chapter_end);
						let now = new Date().getTime();
						if (now < begin) {
							html += `
							<div class="timetable_column">未开课</div>
						</div>`;
						}
						if (now >= begin && now <= end) {
							html += `
							<div class="timetable_column">进行中</div>
						</div>`;
						}
						if (now > end) {
							html += `
							<div class="timetable_column">已结束</div>
						</div>`;
						}
					}
				}
				$("#timetable").html(html);
			}else {
				$("#timetable").html("<div class='notice'>发生错误，请联系网站工作人员！</div>");
			}
		})
	}
	let beginStr = getDateString(calendar.view.activeStart);
	let endStr = getDateString(calendar.view.activeEnd);
	let todayStr = getDateString(new Date());
	renderTimetable(beginStr, endStr, todayStr);
	// 点击日历板的每个日期单元格
	$("body").on("click","#calendar .fc-content-skeleton td.fc-day-top",function(){
		let index = $(this).index();
		$(this).parents(".fc-content-skeleton").prev(".fc-bg")
			.find("td.fc-day").eq(index).trigger("click");
	})
	$("body").on("click","#calendar .fc-bg td.fc-day",function(){
		$this = $(this);
		let selectedDate = new Date($this.attr("data-date"));
		$(".fc-highlight").removeClass("fc-highlight");
		$this.addClass("fc-highlight");
		setYearMonthDateDay(selectedDate);
		// 渲染当日课程安排
		let beginStr = getDateString(calendar.view.activeStart);
		let endStr = getDateString(calendar.view.activeEnd);
		let todayStr = getDateString(selectedDate);
		renderTimetable(beginStr, endStr, todayStr);
	})
	// 点击[<]按钮
	$("body").on("click",".fc-left .fc-prev-button",function(){
		setYearMonthDateDay();
		$(".custom_container").attr("class","custom_container normal");
		$(".fc-row.hidden").removeClass("hidden");
		// 展示当前月份的安排情况，清空课程安排
		let beginStr = getDateString(calendar.view.activeStart);
		let endStr = getDateString(calendar.view.activeEnd);
		renderTimetable(beginStr, endStr, "");
	})
	// 点击[>]按钮
	$("body").on("click",".fc-right .fc-next-button",function(){
		setYearMonthDateDay();
		$(".custom_container").attr("class","custom_container normal");
		$(".fc-row.hidden").removeClass("hidden");
		// TODO 展示当前月份的安排情况，清空课程安排
		let beginStr = getDateString(calendar.view.activeStart);
		let endStr = getDateString(calendar.view.activeEnd);
		renderTimetable(beginStr, endStr, "");
	})
	// 点击[今天]按钮
	$("body").on("click",".custom_container .button_wrap > span",function(){
		calendar.today();
		$(".fc-highlight").removeClass("fc-highlight");
		setYearMonthDateDay(new Date());
		$(".custom_container").attr("class","custom_container normal");
		$(".fc-row.hidden").removeClass("hidden");
		// 渲染今天的课程安排
		let beginStr = getDateString(calendar.view.activeStart);
		let endStr = getDateString(calendar.view.activeEnd);
		let todayStr = getDateString(new Date());
		renderTimetable(beginStr, endStr, todayStr);
		
	})
	// 点击[∧][∨]按钮(展开和收起日历板)
	$("body").on("click",".custom_container + .button_wrap", function(){
		let $this = $(this);
		let $customContainer = $this.prev(".custom_container")
		if ($customContainer.hasClass("normal")) {
			// 收起日历板
			$customContainer.attr("class","custom_container small");
			let $highlight = $(".fc-highlight");
			if ($highlight.length == 0) {
				// 没有选中日期
				if ($(".fc-today").length == 0) {
					// 日历板中没有今天，保留日历第一行，隐藏其他行
					$(".fc-day-grid .fc-row:not(:first-child)").addClass("hidden");
				}else {
					// 日历板中有今天，保留本周的行，隐藏其他行
					$(".fc-today").parents(".fc-row").siblings(".fc-row").addClass("hidden");
				}
			}else {
				// 选中了日期，保留日期所在行，隐藏其他行
				$highlight.parents(".fc-row").siblings(".fc-row").addClass("hidden");
			}
		}else if ($customContainer.hasClass("small")) {
			// 展开日历板
			$customContainer.attr("class","custom_container normal");
			$(".fc-row.hidden").removeClass("hidden");
		}
	})
	
	
})