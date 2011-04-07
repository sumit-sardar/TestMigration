var runtime_codebase = "http://dl.javafx.com/1.3/";
var runtime_url = "http://dl.javafx.com/1.3/";
var idCounter = 0;
var pingEnable = false;
var timedPings = 10;
var timedPingsInterval = 10;
var appletIdTime = {};
var showStartup = false;

function dhtmlLoadScript(url) {
    var e = document.createElement("script");
    e.src = url;
    e.type = "text/javascript";
    document.getElementsByTagName("head")[0].appendChild(e);
}
function takeTimeStamp() {
    if (pingEnable) {
        var dtId = "deployJavaApplet" + (++idCounter);
        appletIdTime[dtId] = new Date().getTime();
    }
    return dtId;
}
function processTemplate(errMessage) {
    errMessage = errMessage.replace("MACOS_VERSION", dtfxObject.getMacOSVersion());
    errMessage = errMessage.replace("JAVA_VERSION", dtfxObject.getJavaVersion());
    return errMessage;
}
function sendPing(dtId, pingType) {
    if (pingEnable && appletIdTime[dtId] != 0) {
        var startTime = appletIdTime[dtId];
        var diffTime = new Date().getTime() - startTime;
        var href = runtime_url + "ping.js?t=" + pingType + "&id=" + dtId + "&v=1.3.1_b101&tm=" + startTime + "&d=" + diffTime + "&j=" + dtfxObject.thisJavaVersion;
        if (appletIdTime[dtId] != 0) {
            dhtmlLoadScript(href);
        }
        if (pingType.substr(0, 4) == "done") {
            if (showStartup) {
                var el = document.getElementById(dtId);
                if (el != null) {
                    var child = document.createTextNode('Startup time: ' + diffTime);
                    el.appendChild(document.createElement('p'));
                    el.appendChild(child);
                }
            }
            appletIdTime[dtId] = 0;
        }
    }
}
function setupPeriodicPing() {
    if (pingEnable && timedPings > 0) {
        window.setTimeout(sendPeriodicPing, timedPingsInterval * 1000);
        timedPings--;
    }
}
function sendPeriodicPing() {
    if (pingEnable && timedPings >= 0) {
        var unknownApplets = 0;
        for (var id in appletIdTime) {
            if (appletIdTime[id] != 0) {
                sendPing(id, "timed" + timedPings);
                unknownApplets++;
            }
        }
        if (unknownApplets > 0) {
            setupPeriodicPing();
        }
    }
}
var _DTFX_JS_;
if (typeof(_DTFX_JS_) == "undefined") {
    _DTFX_JS_ = "Already Loaded";
    var dtfxObject = {
        browserIDs: [{
            id: "MSIE",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["MSIE"]
        }, {
            id: "Chrome",
            varsToSearch: [navigator.userAgent, navigator.vendor],
            stringsToFind: ["Chrome", "Google"]
        }, {
            id: "Safari",
            varsToSearch: [navigator.userAgent, navigator.vendor],
            stringsToFind: ["Safari", "Apple Computer"]
        }, {
            id: "Opera",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["Opera"]
        }, {
            id: "Netscape Family",
            varsToSearch: [navigator.appName],
            stringsToFind: ["Netscape"]
        }],
        OSIDs: [{
            id: "Windows",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["Windows"]
        }, {
            id: "Mac",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["Mac OS X"]
        }, {
            id: "Linux",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["Linux"]
        }, {
            id: "SunOS",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["SunOS"]
        }, {
            id: "UNIX",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["X11"]
        }],
        NoJavaOS: [{
            id: "iPhone",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["iPhone"]
        }, {
            id: "iPod",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["iPod"]
        }],
        NoJavaBrowser: [],
        NoJavaDownload: [{
            id: "Mac",
            varsToSearch: [navigator.userAgent],
            stringsToFind: ["Mac OS X"]
        }],
        browsersSupportingDirectJavaAccess: ["Opera", "Netscape Family"],
        browsersSupportingActiveX: ["MSIE"],
        activeXVersionList: ["1.8.0", "1.7.0", "1.6.0", "1.5.0", "1.4.2"],
        findEntryInList: function (listToUse) {
            var myID = null;
            for (var i = 0; i < listToUse.length; i++) {
                var match = true;
                for (var j = 0; j < listToUse[i].varsToSearch.length; j++) {
                    if (listToUse[i].varsToSearch[j].indexOf(listToUse[i].stringsToFind[j], 0) == -1) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    myID = listToUse[i].id;
                    break;
                }
            }
            return myID;
        },
        errorMessages: {
            ErrorMacJavaUpgradeRequired: null,
            ErrorMacOSVersionNotSupported: null,
            ErrorNonMacJavaInstallRequired: null,
            ErrorChrome: null,
            ErrorJavaNotSupportedOS: null,
            ErrorJavaNotSupportedBrowser: null,
            ErrorJavaNotSupportedOpera1010: null
        },
        generateInPlaceErrorDisplay: function (displayMessage) {
            var tagLeadChar = "<";
            var tagEndChar = ">";
            dtfxObject.smallErrorCode = tagLeadChar + 'a href="http://java.com/"' + tagEndChar;
            dtfxObject.smallErrorCode += tagLeadChar + 'img src="' + runtime_url + 'java-coffee-cup-23x20.png' + '" border="0" width="23" height="20" alt="Java Coffee Cup"' + tagEndChar;
            var stringOutput = dtfxObject.smallErrorCode;
            stringOutput += displayMessage;
            stringOutput += tagLeadChar + '/a' + tagEndChar;
            dtfxObject.smallErrorCode += tagLeadChar + '/a' + tagEndChar;
            return stringOutput;
        },
        generatePopupErrorDisplay: function (displayMessage, popupMessageToUser) {
            var tagLeadChar = "<";
            var tagEndChar = ">";
            dtfxObject.smallErrorCode = tagLeadChar + 'a href="javascript:dtfxObject.explainAndInstall(' + "'" + popupMessageToUser + "'" + ')"' + tagEndChar;
            dtfxObject.smallErrorCode += tagLeadChar + 'img src="' + runtime_url + 'java-coffee-cup-23x20.png' + '" border="0" width="23" height="20" alt="Java Coffee Cup"' + tagEndChar;
            var stringOutput = dtfxObject.smallErrorCode;
            stringOutput += displayMessage;
            stringOutput += tagLeadChar + '/a' + tagEndChar;
            dtfxObject.smallErrorCode += tagLeadChar + '/a' + tagEndChar;
            return stringOutput;
        },
        initErrorMsg: function () {
            dtfxObject.errorMessages.ErrorJavaNotSupportedBrowser = dtfxObject.generatePopupErrorDisplay(" The application could not load because your browser does not support Java. Click for more options.", "Please user another browser like Mozilla Firefox to view the application.");
            dtfxObject.errorMessages.ErrorJavaNotSupportedOpera1010 = dtfxObject.generatePopupErrorDisplay(" The application could not load because your browser does not support Java. Click for more options.", "Please upgrade your Opera to 10.50 and above to view the application.");
            dtfxObject.errorMessages.ErrorChrome = dtfxObject.generatePopupErrorDisplay(" A newer version of Java is needed to view the application. Click to update Java now.");
            dtfxObject.errorMessages.ErrorJavaNotSupportedOS = dtfxObject.generateInPlaceErrorDisplay(" The application uses Java, but Java is not supported by your system. Use a computer with another operating system to view this applicaiton.");
            dtfxObject.errorMessages.ErrorNonMacJavaInstallRequired = dtfxObject.generatePopupErrorDisplay(" A newer version of Java is needed to view the application. Click to update Java.", "Click to update Java.");
            dtfxObject.errorMessages.ErrorMacJavaUpgradeRequired = dtfxObject.generatePopupErrorDisplay(" The application could not load. Click for details.", "JavaFX requires Java 5.0 (1.5) or above. Please use Software Update to upgrade your Java version.");
            dtfxObject.errorMessages.ErrorMacOSVersionNotSupported = dtfxObject.generatePopupErrorDisplay(" The application could not load. Click for details.", "The application requires Mac OS 10.5 or newer. Please upgrade your OS to view the application. JavaFX requires Java 5.0 (1.5) or above.");
        },
        thisBrowser: null,
        getBrowser: function () {
            if (null === dtfxObject.thisBrowser) {
                dtfxObject.thisBrowser = dtfxObject.findEntryInList(dtfxObject.browserIDs);
                if (null === dtfxObject.thisBrowser) {
                    dtfxObject.thisBrowser = "unknown";
                }
            }
            return dtfxObject.thisBrowser;
        },
        thisBrowserCanAccessJava: null,
        browserCanAccessJava: function () {
            if (null === dtfxObject.thisBrowserCanAccessJava) {
                var browser = dtfxObject.getBrowser();
                dtfxObject.thisBrowserCanAccessJava = false;
                for (var i = 0; i < dtfxObject.browsersSupportingDirectJavaAccess.length; ++i) {
                    if (browser == dtfxObject.browsersSupportingDirectJavaAccess[i]) {
                        dtfxObject.thisBrowserCanAccessJava = true;
                        break;
                    }
                }
            }
            return dtfxObject.thisBrowserCanAccessJava;
        },
        thisBrowserHasActiveX: null,
        browserHasActiveX: function () {
            if (null === dtfxObject.thisBrowserHasActiveX) {
                var browser = dtfxObject.getBrowser();
                dtfxObject.thisBrowserHasActiveX = false;
                if (null != window.ActiveXObject) {
                    for (var i = 0; i < dtfxObject.browsersSupportingActiveX.length; ++i) {
                        if (browser == dtfxObject.browsersSupportingActiveX[i]) {
                            dtfxObject.thisBrowserHasActiveX = true;
                            break;
                        }
                    }
                }
            }
            return dtfxObject.thisBrowserHasActiveX;
        },
        thisJavaVersion: null,
        getJavaVersion: function () {
            if (null === dtfxObject.thisJavaVersion) {
                if ((null === dtfxObject.thisJavaVersion) && (dtfxObject.browserHasActiveX())) {
                    for (var v = 0; v < dtfxObject.activeXVersionList.length; v++) {
                        try {
                            var axo = new ActiveXObject("JavaWebStart.isInstalled." + dtfxObject.activeXVersionList[v] + ".0");
                            dtfxObject.thisJavaVersion = dtfxObject.activeXVersionList[v];
                            break;
                        } catch (ignored) {}
                    }
                }
                if (null === dtfxObject.thisJavaVersion) {
                    var bestVersionSeen = null;
                    for (var i = 0; i < navigator.mimeTypes.length; i++) {
                        var s = navigator.mimeTypes[i].type;
                        var m = s.match(/^application\/x-java-applet;jpi-version=(.*)$/);
                        if (m !== null) {
                            dtfxObject.thisJavaVersion = m[1];
                            break;
                        }
                        m = s.match(/^application\/x-java-applet;version=(.*)$/);
                        if (m !== null) {
                            if ((null === bestVersionSeen) || (m[1] > bestVersionSeen)) {
                                bestVersionSeen = m[1];
                            }
                        }
                    }
                    if ((null === dtfxObject.thisJavaVersion) && (null !== bestVersionSeen)) {
                        dtfxObject.thisJavaVersion = bestVersionSeen;
                    }
                }
                if ((null === dtfxObject.thisJavaVersion) && dtfxObject.browserCanAccessJava() && (typeof java == "object")) {
                    dtfxObject.thisJavaVersion = java.lang.System.getProperty("java.version");
                }
                if (null === dtfxObject.thisJavaVersion) {
                    dtfxObject.thisJavaVersion = "0 - unknown";
                }
            }
            return dtfxObject.thisJavaVersion;
        },
        thisOSName: null,
        getSystemOS: function () {
            if (null === dtfxObject.thisOSName) {
                dtfxObject.thisOSName = dtfxObject.findEntryInList(dtfxObject.OSIDs);
                if (null === dtfxObject.thisOSName) {
                    dtfxObject.thisOSName = "unknown";
                }
            }
            return dtfxObject.thisOSName;
        },
        thisMacOSVersion: null,
        getMacOSVersion: function () {
            if (null === dtfxObject.thisMacOSVersion) {
                if ("Mac" != dtfxObject.getSystemOS()) {
                    dtfxObject.thisMacOSVersion = "Not Mac";
                } else {
                    if (dtfxObject.browserCanAccessJava()) {
                        dtfxObject.thisMacOSVersion = java.lang.System.getProperty("os.version");
                    }
                    if (null === dtfxObject.thisMacOSVersion) {
                        var av = navigator.appVersion;
                        var m = av.match(/Mac OS X ([0-9_]*);/);
                        if (null !== m) {
                            dtfxObject.thisMacOSVersion = m[1];
                            dtfxObject.thisMacOSVersion = dtfxObject.thisMacOSVersion.split("_").join(".");
                        }
                    }
                }
                if (null === dtfxObject.thisMacOSVersion) {
                    dtfxObject.thisMacOSVersion = "unknown";
                }
            }
            return dtfxObject.thisMacOSVersion;
        },
        thisOperaVersion: null,
        getOperaVersion: function () {
            if (null === dtfxObject.thisOperaVersion) {
                if ("Opera" != dtfxObject.getBrowser()) {
                    dtfxObject.thisOperaVersion = "Not Opera"
                } else {
                    var ua = navigator.userAgent;
                    var m = ua.match(/Version\/\d+\.\d+/gi);
                    if (null !== m) {
                        dtfxObject.thisOperaVersion = m[0].split("/")[1];
                    }
                    if (null === dtfxObject.thisOperaVersion) {
                        dtfxObject.thisOperaVersion = "unknown";
                    }
                }
            }
            return dtfxObject.thisOperaVersion;
        },
        overlayCount: 0,
        nameSeed: 0,
        getBogusJarFileName: function () {
            if (0 === dtfxObject.nameSeed) {
                dtfxObject.nameSeed = (new Date()).getTime();
            }
            var uniqueNum = dtfxObject.nameSeed++;
            return "emptyJarFile-" + uniqueNum;
        },
        isVersionAvailable: function () {
            var ret = true;
            if (("Safari" == dtfxObject.getBrowser()) && ("Mac" == dtfxObject.getSystemOS()) && (dtfxObject.getMacOSVersion().indexOf("10.4", 0) === 0)) {
                ret = false;
            }
            return ret;
        },
        javaSupport: null,
        getJavaSupportOS: function () {
            if (null === dtfxObject.javaSupport) {
                var noSupportName = dtfxObject.findEntryInList(dtfxObject.NoJavaOS);
                if (null === noSupportName) {
                    dtfxObject.javaSupport = true;
                } else {
                    dtfxObject.javaSupport = false;
                }
            }
            return dtfxObject.javaSupport;
        },
        javaSupportBrowser: null,
        getJavaSupportBrowser: function () {
            if (null === dtfxObject.javaSupportBrowser) {
                dtfxObject.javaSupportBrowser = "true";
                var noSupportName = dtfxObject.findEntryInList(dtfxObject.NoJavaBrowser);
                if (null === noSupportName) {
                    if ("Opera" == dtfxObject.getBrowser()) {
                        var operaVersion = dtfxObject.getOperaVersion().split(".");
                        if ((operaVersion[0] == 10) && (operaVersion[1] < 50)) {
                            dtfxObject.javaSupportBrowser = "Opera" + operaVersion[0] + "." + operaVersion[1];
                        }
                    }
                } else {
                    dtfxObject.javaSupportBrowser = noSupportName;
                }
            }
            return dtfxObject.javaSupportBrowser;
        },
        javaDownloadSupport: null,
        getJavaDownloadSupportExists: function () {
            if (null === dtfxObject.javaDownloadSupport) {
                var noSupportName = dtfxObject.findEntryInList(dtfxObject.NoJavaDownload);
                if (null === noSupportName) {
                    dtfxObject.javaDownloadSupport = true;
                } else {
                    dtfxObject.javaDownloadSupport = false;
                }
            }
            return dtfxObject.javaDownloadSupport;
        },
        errorMessageBoxes: null,
        errorMessageWidths: null,
        errorMessageHeights: null,
        onloadHandlerQueued: false,
        smallErrorCode: "",
        onloadCheckErrorDisplay: function () {
            var boxId;
            var width;
            var height;
            while (dtfxObject.errorMessageBoxes.length > 0) {
                boxId = dtfxObject.errorMessageBoxes.pop();
                width = dtfxObject.errorMessageWidths.pop();
                height = dtfxObject.errorMessageHeights.pop();
                var tableForBox = document.getElementById(boxId);
                if ((tableForBox.offsetHeight != height) || (tableForBox.offsetWidth != width)) {
                    tableForBox.rows.item(0).cells.item(0).innerHTML = dtfxObject.smallErrorCode;
                }
            }
        },
        javafxString: function (launchParams, appletParams) {
            var stringOutput = "";
            var errorMessageToUser = "";
            var appletID = takeTimeStamp();
            if (!dtfxObject.getJavaSupportOS()) {
                errorMessageToUser = dtfxObject.errorMessages.ErrorJavaNotSupportedOS;
                sendPing(appletID, "done_unsupportedbyjre");
            } else if ("true" !== dtfxObject.getJavaSupportBrowser()) {
                var browser = dtfxObject.getJavaSupportBrowser();
                if (null !== browser.match(/Opera10/i)) {
                    errorMessageToUser = dtfxObject.errorMessages.ErrorJavaNotSupportedOpera1010;
                } else {
                    errorMessageToUser = dtfxObject.errorMessages.ErrorJavaNotSupportedBrowser;
                }
                sendPing(appletID, "done_unsupportedbrowser");
            } else if (dtfxObject.isVersionAvailable()) {
                var javaVersion = dtfxObject.getJavaVersion();
                sendPing(appletID, "start");
                if (("V" + javaVersion) < "V1.5") {
                    if ("Mac" == dtfxObject.getSystemOS()) {
                        var osVersion = dtfxObject.getMacOSVersion();
                        if (("V" + osVersion) < "V10.4") {
                            errorMessageToUser = dtfxObject.errorMessages.ErrorMacOSVersionNotSupported;
                            sendPing(appletID, "done_oldmac");
                        } else {
                            errorMessageToUser = dtfxObject.errorMessages.ErrorMacJavaUpgradeRequired;
                            sendPing(appletID, "done_oldjremac");
                        }
                    } else {
                        if ("Chrome" === dtfxObject.getBrowser()) {
                            errorMessageToUser = dtfxObject.errorMessages.ErrorChrome;
                        } else {
                            errorMessageToUser = dtfxObject.errorMessages.ErrorNonMacJavaInstallRequired;
                        }
                        if ("0 - unknown" == javaVersion) {
                            sendPing(appletID, "done_nojre");
                        } else {
                            sendPing(appletID, "done_oldjre");
                        }
                    }
                }
            } else {
                sendPing(appletID, "start2");
            }
            var standardArchives = [];
            var standardArchivesPrepend = [];
            switch (dtfxObject.getSystemOS()) {
            case "Mac":
                standardArchives.push("javafx-rt-macosx-universal");
                standardArchives.push("javafx-rt-lazy-macosx-universal");
                standardArchives.push("javafx-rt-fonts-macosx-universal");
                standardArchivesPrepend.push('applet-launcher-macosx-universal');
                break;
            case "Windows":
                standardArchives.push("javafx-rt-windows-i586");
                standardArchives.push("javafx-rt-lazy-windows-i586");
                standardArchives.push("javafx-rt-fonts-windows-i586");
                standardArchivesPrepend.push('applet-launcher-windows-i586');
                break;
            case "Linux":
                standardArchives.push("javafx-rt-linux-i586");
                standardArchives.push("javafx-rt-lazy-linux-i586");
                standardArchives.push("javafx-rt-fonts-linux-i586");
                standardArchivesPrepend.push('applet-launcher-linux-i586');
                break;
            case "SunOS":
                standardArchives.push("javafx-rt-solaris-i586");
                standardArchives.push("javafx-rt-lazy-solaris-i586");
                standardArchives.push("javafx-rt-fonts-solaris-i586");
                standardArchivesPrepend.push('applet-launcher-solaris-i586');
                break;
            }
            standardArchives.push("" + dtfxObject.getBogusJarFileName());
            var versionNumber = "1.3.1_b101";
            var appletPlayer = "org.jdesktop.applet.util.JNLPAppletLauncher";
            var tagLeadChar = "<";
            var tagEndChar = ">";
            var carriageReturn = "\n";
            var appletTagParams = {};
            appletTagParams.code = appletPlayer;
            var params = {};
            params.codebase_lookup = "false";
            params["subapplet.classname"] = "com.sun.javafx.runtime.adapter.Applet";
            params.progressbar = "false";
            params.classloader_cache = "false";
            var loading_image_url = null;
            var loading_image_width = -1;
            var loading_image_height = -1;
            var loading_background = "white";
            var app_version = null;
            var archive = "";
            var key = "";
            if (typeof launchParams != "string") {
                for (key in launchParams) {
                    switch (key.toLocaleLowerCase()) {
                    case "archive":
                        archive = launchParams[key];
                        break;
                    case "app_version":
                        app_version = launchParams[key];
                        break;
                    case "jnlp_href":
                        params.jnlp_href = launchParams[key];
                        break;
                    case "version":
                        versionNumber = launchParams[key];
                        break;
                    case "code":
                        params.MainJavaFXScript = launchParams[key];
                        break;
                    case "name":
                        params["subapplet.displayname"] = launchParams[key];
                        break;
                    case "draggable":
                        params[key] = launchParams[key];
                        break;
                    case "displayhtml":
                        if (launchParams[key]) {
                            tagLeadChar = "&lt;";
                            tagEndChar = "&gt;";
                            carriageReturn = "<br>\n";
                        }
                        break;
                    case "loading_image_url":
                        loading_image_url = launchParams[key];
                        break;
                    case "loading_image_width":
                        loading_image_width = launchParams[key];
                        break;
                    case "loading_image_height":
                        loading_image_height = launchParams[key];
                        break;
                    case "loading_background":
                        loading_background = launchParams[key];
                        break;
                    default:
                        appletTagParams[key] = launchParams[key];
                        break;
                    }
                }
            } else {
                archive = launchParams;
            }
            if (errorMessageToUser != "") {
                var errId = "errorWithJava" + (++dtfxObject.overlayCount);
                var w = appletTagParams.width;
                var h = appletTagParams.height;
                stringOutput += tagLeadChar + 'div id="JavaLaunchError" style="width:' + w + ';height:' + h + ';background:white"' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'table id="' + errId + '" width=' + w + ' height=' + h + ' border=1 padding=0 margin=0' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'tr' + tagEndChar + tagLeadChar + 'td align="center" style="vertical-align: middle;" valign="middle"' + tagEndChar + carriageReturn;
                stringOutput += processTemplate(errorMessageToUser);
                stringOutput += tagLeadChar + '/td' + tagEndChar + tagLeadChar + '/tr' + tagEndChar + tagLeadChar + '/table' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + '/div' + tagEndChar + carriageReturn;
                dtfxObject.errorMessageBoxes.push(errId);
                dtfxObject.errorMessageWidths.push(w);
                dtfxObject.errorMessageHeights.push(h);
                if (!dtfxObject.onloadHandlerQueued) {
                    if (window.attachEvent) {
                        window.attachEvent("onload", dtfxObject.onloadCheckErrorDisplay);
                    } else if (window.addEventListener) {
                        window.addEventListener("load", dtfxObject.onloadCheckErrorDisplay, false);
                    } else {
                        document.addEventListener("load", dtfxObject.onloadCheckErrorDisplay, false);
                    }
                }
                return stringOutput;
            }
            params["jnlpNumExtensions"] = 0;
            if (params.jnlp_href === undefined) {
                var loc = archive.indexOf(".jar,");
                if (-1 == loc) {
                    loc = archive.lastIndexOf(".jar");
                }
                if (-1 != loc) {
                    params.jnlp_href = archive.substr(0, loc) + "_browser.jnlp";
                }
            }
            if (app_version == null) {
                appletTagParams.archive = archive;
            } else {
                appletTagParams.archive = "";
                var numOfJars = archive.split(",").length;
                params.cache_archive = archive;
                params.cache_version = "";
                for (var i = 0; i < numOfJars; i++) {
                    params.cache_version += app_version + ",";
                }
            }
            var archive = "";
            for (var i = 0; i < standardArchivesPrepend.length; i++) {
                if (i > 0) {
                    archive += ",";
                }
                archive += runtime_codebase + standardArchivesPrepend[i];
                if (versionNumber !== "") {
                    archive += "__V" + versionNumber;
                }
                archive += ".jar";
            }
            appletTagParams.archive = archive + "," + appletTagParams.archive;
            for (var i = 0; i < standardArchives.length; i++) {
                appletTagParams.archive += "," + runtime_codebase + standardArchives[i];
                if (versionNumber !== "") {
                    appletTagParams.archive += "__V" + versionNumber;
                }
                appletTagParams.archive += ".jar";
            }
            if (dtfxObject.fxOverlayEnabled()) {
                var dtId = "deployJavaApplet" + (++dtfxObject.overlayCount);
                params["deployJavaAppletID"] = dtId;
                var width = appletTagParams.width;
                var height = appletTagParams.height;
                var img;
                var imgWidth;
                var imgHeight;
                window.setTimeout(function () {
                    hideOverlay(dtId);
                    sendPing(dtId, "done_timeout");
                }, 180 * 1000);
                if (loading_image_url !== null && loading_image_height > 0 && loading_image_width > 0) {
                    img = loading_image_url;
                    imgWidth = loading_image_width;
                    imgHeight = loading_image_height;
                } else {
                    img = runtime_url;
                    if (width >= 100 && height >= 100) {
                        img += 'javafx-loading-100x100.gif';
                        imgWidth = 80;
                        imgHeight = 80;
                    } else {
                        img += 'javafx-loading-25x25.gif';
                        imgWidth = 25;
                        imgHeight = 25;
                    }
                }
                stringOutput += tagLeadChar + 'div id="' + dtId + 'Overlay' + '" style="width:' + width + ';height:' + height + ';position:absolute;background:' + loading_background + '"' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'table width=' + width + ' height=' + height + ' border=0 cellpadding=0' + ' style="border-width:0px;border-spacing:0px 0px;margin:0px;padding:0px;"' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'tr' + tagEndChar + tagLeadChar + 'td align="center" valign="middle" style="vertical-align: middle;"' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'img src="' + img + '" alt="" width=' + imgWidth + ' height=' + imgHeight + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + '/td' + tagEndChar + tagLeadChar + '/tr' + tagEndChar + tagLeadChar + '/table' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + '/div' + tagEndChar + carriageReturn;
                stringOutput += tagLeadChar + 'div id="' + dtId + '" style="position:relative;left:-10000px"' + tagEndChar + carriageReturn;
            }
            stringOutput += tagLeadChar + "APPLET MAYSCRIPT" + carriageReturn;
            for (key in appletTagParams) {
                stringOutput += key + "=";
                if (typeof appletTagParams[key] == "number") {
                    stringOutput += appletTagParams[key];
                } else {
                    stringOutput += "\"" + appletTagParams[key] + "\"";
                }
                stringOutput += carriageReturn;
            }
            stringOutput += tagEndChar + carriageReturn;
            if (appletParams) {
                for (key in appletParams) {
                    params[key] = appletParams[key];
                }
            }
            if (pingEnable) {
                params["pingAppletID"] = appletID;
                params["fxbuild"] = '1.3.1_b101';
                params["pingTS"] = appletIdTime[appletID];
            }
            for (key in params) {
                stringOutput += tagLeadChar + "param name=\"" + key + "\" value=\"" + params[key] + "\"" + tagEndChar + carriageReturn;
            }
            stringOutput += tagLeadChar + "/APPLET" + tagEndChar + carriageReturn;
            if (dtfxObject.fxOverlayEnabled()) {
                stringOutput += tagLeadChar + "/div" + tagEndChar + carriageReturn;
            }
            return stringOutput;
        },
        fxOverlayEnabled: function () {
            return (dtfxObject.getBrowser() != "Netscape Family" && dtfxObject.getBrowser() != "Opera") || dtfxObject.getSystemOS() != "Mac";
        },
        explainAndInstall: function (explanation) {
            var startJREInstall = function () {
                deployJava.do_initialize();
                deployJava.returnPage = document.location;
                deployJava.installLatestJRE();
            };
            if (dtfxObject.getJavaDownloadSupportExists() && dtfxObject.getJavaSupportOS() && ("true" == dtfxObject.getJavaSupportBrowser())) {
                if (typeof(deployJava) == 'undefined') {
                    if (dtfxObject.getBrowser() == "MSIE") {
                        var script = document.createElement('SCRIPT');
                        script.type = 'text/javascript';
                        script.src = 'http://java.com/js/deployJava.js';
                        var head = document.getElementsByTagName('HEAD');
                        if (head[0] != null) head[0].appendChild(script);
                        script.onreadystatechange = startJREInstall;
                        script.onload = startJREInstall;
                    } else {
                        location.href = 'http://java.sun.com/webapps/getjava/BrowserRedirect?host=java.com';
                    }
                } else {
                    startJREInstall();
                }
            } else {
                alert(explanation);
            }
        },
        initDtfx: function () {
            dtfxObject.errorMessageBoxes = new Array();
            dtfxObject.errorMessageWidths = new Array();
            dtfxObject.errorMessageHeights = new Array();
            window.onunload = function () {
                appletIdTime = {};
            };
            dtfxObject.initErrorMsg();
        }
    };
    dtfxObject.initDtfx();
}
function javafx(launchParams, appletParams) {
    if (idCounter == 0) {
        window.setTimeout(sendPeriodicPing, 4000);
    }
    var stringOutput = dtfxObject.javafxString(launchParams, appletParams);
    if (null != stringOutput) {
        document.write(stringOutput);
    }
}
function javafxString(launchParams, appletParams) {
    return dtfxObject.javafxString(launchParams, appletParams);
}
function hideOverlay(id) {
    var olay = document.getElementById(id + "Overlay");
    if (olay) {
        if (olay.parentNode && olay.parentNode.removeChild) {
            olay.parentNode.removeChild(olay);
        } else {
            olay.style.visibility = "hidden";
        }
        document.getElementById(id).style.left = "0px";
    }
}
function fxAppletStarted(id) {
    hideOverlay(id);
    sendPing(id, "done_ok");
}
function fxAppletHideSplash(id) {
    hideOverlay(id);
    sendPing(id, "info_splash");
}
function fxAppletFailed(id, reason) {
    hideOverlay(id);
    sendPing(id, "applet_failed_" + reason);
}