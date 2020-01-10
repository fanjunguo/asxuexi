SYT = "年";
SMT = "月";
SDT = "日";
BYN = 1;
AYN = 60;
function YMDselect() {
	this.SelY = document.getElementsByName(arguments[0])[0];
	this.SelM = document.getElementsByName(arguments[1])[0];
	this.SelD = document.getElementsByName(arguments[2])[0];
	this.DefY = this.SelD ? arguments[3] : arguments[2];
	this.DefM = this.SelD ? arguments[4] : arguments[3];
	this.DefD = this.SelD ? arguments[5] : arguments[4];
	this.SelY.YMD = this;
	this.SelM.YMD = this;
	this.SelY.onchange = function() {
		YMDselect.SetM(this.YMD)
	};
	if (this.SelD)
		this.SelM.onchange = function() {
			YMDselect.SetD(this.YMD)
		};
	YMDselect.SetY(this)
};
YMDselect.SetY = function(YMD) {
	dDate = new Date();
	dCurYear = dDate.getFullYear();
	OptDisabled=new Option(SYT, '');
	OptDisabled.disabled=true;
	OptDisabled.selected=true;
	YMD.SelY.options.add(OptDisabled);
	for (i = dCurYear + AYN; i > (dCurYear - BYN); i--) {
		YMDYT = i;
		YMDYV = i;
		OptY = new Option(YMDYT, YMDYV);
		YMD.SelY.options.add(OptY);
		if (YMD.DefY == YMDYV)
			OptY.selected = true
	}
	YMDselect.SetM(YMD)
};
YMDselect.SetM = function(YMD) {
	YMD.SelM.length = 0;
	OptDisabled=new Option(SMT, '');
	OptDisabled.disabled=true;
	OptDisabled.selected=true;
	YMD.SelM.options.add(OptDisabled);
	if (YMD.SelY.value > 0) {
		for (var i = 1; i <= 12; i++) {
			if (i<10) {
				YMDMT = "0"+i;
			}else{
				YMDMT=i;
			}
			YMDMV = i;
			OptM = new Option(YMDMT, YMDMV);
			YMD.SelM.options.add(OptM);
			if (YMD.DefM == YMDMV)
				OptM.selected = true
		}
	}
	if (YMD.SelD)
		YMDselect.SetD(YMD)
};
YMDselect.SetD = function(YMD) {
	YI = YMD.SelY.value;
	MI = YMD.SelM.value;
	YMD.SelD.length = 0;
	OptDisabled=new Option(SDT, '');
	OptDisabled.disabled=true;
	OptDisabled.selected=true;
	YMD.SelD.options.add(OptDisabled);
	if (YI > 0 && MI > 0) {
		dPrevDate = new Date(YI, MI, 0);
		daysInMonth = dPrevDate.getDate();
		for (d = 1; d <= parseInt(daysInMonth); d++) {
			if (d<10) {
				YMDDT = "0"+d;
			}else{
				YMDDT = d;
			}
			YMDDV = d;
			OptD = new Option(YMDDT, YMDDV);
			YMD.SelD.options.add(OptD);
			if (YMD.DefD == YMDDV)
				OptD.selected = true
		}
	}
}