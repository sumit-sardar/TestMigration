var timerID = null;
var timerRunning = false;
var timeValue = 500;  //the time increment in mS
var count = 0;
var finish = false;

function startFileUpload()
{    
	initImages();
	startProgressBar();        
}
 
function initImages() 
{
	image00 = new Image(); image00.src='/OrganizationWeb/resources/images/status/progress.gif';
	image01 = new Image(); image01.src='/OrganizationWeb/resources/images/status/progress01.gif';
	image02 = new Image(); image02.src='/OrganizationWeb/resources/images/status/progress02.gif';
	image03 = new Image(); image03.src='/OrganizationWeb/resources/images/status/progress03.gif';
	image04 = new Image(); image04.src='/OrganizationWeb/resources/images/status/progress04.gif';
	image05 = new Image(); image05.src='/OrganizationWeb/resources/images/status/progress05.gif';
	image06 = new Image(); image06.src='/OrganizationWeb/resources/images/status/progress06.gif';
	image07 = new Image(); image07.src='/OrganizationWeb/resources/images/status/progress07.gif';
	image08 = new Image(); image08.src='/OrganizationWeb/resources/images/status/progress08.gif';
	image09 = new Image(); image09.src='/OrganizationWeb/resources/images/status/progress09.gif';
	image10 = new Image(); image10.src='/OrganizationWeb/resources/images/status/progress10.gif';
	image11 = new Image(); image11.src='/OrganizationWeb/resources/images/status/progress11.gif';
	image12 = new Image(); image12.src='/OrganizationWeb/resources/images/status/progress12.gif';
}

function startProgressBar() 
{
    startclock();
}

function startclock() 
{
    stopclock();
    timerID = setInterval("increment()", timeValue);
    timerRunning = true;
    document.images.bar.src=image00.src;
    document.body.style.cursor = 'wait';
}

function stopclock() 
{
    if (timerRunning)
        clearInterval(timerID);
    timerRunning = false;
}

function increment() {
    count += 1;
    if (count == 0) {document.images.bar.src=image00.src;}
    if (count == 1) {document.images.bar.src=image01.src;}
    if (count == 2) {document.images.bar.src=image02.src;}
    if (count == 3) {document.images.bar.src=image03.src;}
    if (count == 4) {document.images.bar.src=image04.src;}
    if (count == 5) {document.images.bar.src=image05.src;}
    if (count == 6) {document.images.bar.src=image06.src;}
    if (count == 7) {document.images.bar.src=image07.src;}
    if (count == 8) {document.images.bar.src=image08.src;}
    if (count == 9) {document.images.bar.src=image09.src;}
    if (count == 10) {document.images.bar.src=image10.src;}
    if (count == 11) {document.images.bar.src=image11.src;}
    //If you want it to repeat the bar continuously then use this line:
    if (count == 12) {document.images.bar.src=image12.src; count=-1;}
    //If you want it to stop repeating the bar then use this line:
    //if (count == 12) {document.images.bar.src=image12.src; end();}
}

