/**************************************************************
KindEditor 2.4
WYSIWYG HTML Editor
Copyright (c) 2006 kindsoft.net

Author: Roddy(luolonghao@gmail.com)
Created: 2006/07/20
Last Modified: 2006/08/19
**************************************************************/

var SITE_DOMAIN;
var IMAGE_PATH;
var ICON_PATH;
var IMAGE_ATTACH_PATH;
var IMAGE_UPLOAD_CGI;
var MENU_BORDER_COLOR;
var MENU_BG_COLOR;
var MENU_TEXT_COLOR;
var MENU_SELECTED_COLOR;
var TOOLBAR_BORDER_COLOR;
var TOOLBAR_BG_COLOR;
var FORM_BORDER_COLOR;
var FORM_BG_COLOR;
var BUTTON_COLOR;
var OBJ_NAME;
var SELECTION;
var RANGE;
var RANGE_TEXT;
var EDITFORM_DOCUMENT;
var IMAGE_DOCUMENT;
var FLASH_DOCUMENT;
var MEDIA_DOCUMENT;
var REAL_DOCUMENT;
var LINK_DOCUMENT;
var BROWSER;
var TOOLBAR_ICON;
var EDITOR_TYPE;
var SAFE_MODE;
var UPLOAD_MODE;

var MSG_INPUT_URL = "请输入正确的URL地址。";
var MSG_SELECT_IMAGE = "请选择图片。";
var MSG_INVALID_IMAGE = "只能选择GIF,JPG,PNG,BMP格式的图片，请重新选择。";
var MSG_INVALID_FLASH = "只能选择SWF格式的文件，请重新选择。";
var MSG_INVALID_MEDIA = "只能选择MP3,WAV,WMA,WMV,MID,AVI,MPG,ASF格式的文件，请重新选择。";
var MSG_INVALID_REAL = "只能选择RM,RMVB格式的文件，请重新选择。";
var MSG_INVALID_WIDTH = "宽度不是数字，请重新输入。";
var MSG_INVALID_HEIGHT = "高度不是数字，请重新输入。";
var MSG_INVALID_BORDER = "边框不是数字，请重新输入。";
var MSG_INVALID_HSPACE = "横隔不是数字，请重新输入。";
var MSG_INVALID_VSPACE = "竖隔不是数字，请重新输入。";
var STR_TITLE = "描述";
var STR_WIDTH = "宽";
var STR_HEIGHT = "高";
var STR_BORDER = "边";
var STR_ALIGN = "对齐方式";
var STR_HSPACE = "横隔";
var STR_VSPACE = "竖隔";
var STR_BUTTON_CONFIRM = "确定";
var STR_BUTTON_CANCEL = "取消";
var STR_BUTTON_PREVIEW = "预览";
var STR_BUTTON_LISTENING = "试听";
var STR_IMAGE_LOCAL = "本地";
var STR_IMAGE_REMOTE = "远程";
var STR_LINK_BLANK = "新窗口";
var STR_LINK_NOBLANK = "当前窗口";
var STR_LINK_TARGET = "目标";
var STR_ABOUT = "访问技术支持网站";
var STR_INPUT_CONTENT = "请输入内容";

var EDITOR_FONT_FAMILY = "SimSun";

var FONT_NAME = Array(
					Array('SimSun', '宋体'), 
					Array('SimHei', '黑体'), 
					Array('FangSong_GB2312', '仿宋体'), 
					Array('KaiTi_GB2312', '楷体'), 
					Array('NSimSun', '新宋体'), 
					Array('Arial', 'Arial'), 
					Array('Arial Black', 'Arial Black'), 
					Array('Times New Roman', 'Times New Roman'), 
					Array('Courier New', 'Courier New'), 
					Array('Tahoma', 'Tahoma'), 
					Array('Verdana', 'Verdana'), 
					Array('GulimChe', 'GulimChe'), 
					Array('MS Gothic', 'MS Gothic') 
					);
var ZOOM_TABLE = Array('250%', '200%', '150%', '120%', '100%', '80%', '50%');
var TITLE_TABLE = Array(
					Array('H1', '标题 1'), 
					Array('H2', '标题 2'), 
					Array('H3', '标题 3'), 
					Array('H4', '标题 4'), 
					Array('H5', '标题 5'), 
					Array('H6', '标题 6')
					);
var FONT_SIZE = Array(
					Array(1,'8pt'), 
					Array(2,'10pt'), 
					Array(3,'12pt'), 
					Array(4,'14pt'), 
					Array(5,'18pt'), 
					Array(6,'24pt'), 
					Array(7,'36pt')
				);
var SPECIAL_CHARACTER = Array('§','№','☆','★','○','●','◎','◇','◆','□','℃','‰','■','△','▲','※',
							'→','←','↑','↓','〓','¤','°','＃','＆','＠','＼','︿','＿','￣','―','α',
							'β','γ','δ','ε','ζ','η','θ','ι','κ','λ','μ','ν','ξ','ο','π','ρ',
							'σ','τ','υ','φ','χ','ψ','ω','≈','≡','≠','＝','≤','≥','＜','＞','≮',
							'≯','∷','±','＋','－','×','÷','／','∫','∮','∝','∞','∧','∨','∑','∏',
							'∪','∩','∈','∵','∴','⊥','∥','∠','⌒','⊙','≌','∽','〖','〗','【','】','（','）','［','］');
var TOP_TOOLBAR_ICON = Array(
						Array('KIND_SOURCE', 'source.gif', '视图转换'),
						Array('KIND_PREVIEW', 'preview.gif', '预览'),
						Array('KIND_ZOOM', 'zoom.gif', '显示比例'),
						Array('KIND_PRINT', 'print.gif', '打印'),
						Array('KIND_UNDO', 'undo.gif', '回退'),
						Array('KIND_REDO', 'redo.gif', '前进'),
						Array('KIND_CUT', 'cut.gif', '剪切'),
						Array('KIND_COPY', 'copy.gif', '复制'),
						Array('KIND_PASTE', 'paste.gif', '粘贴'),
						Array('KIND_SELECTALL', 'selectall.gif', '全选'),
						Array('KIND_JUSTIFYLEFT', 'justifyleft.gif', '左对齐'),
						Array('KIND_JUSTIFYCENTER', 'justifycenter.gif', '居中'),
						Array('KIND_JUSTIFYRIGHT', 'justifyright.gif', '右对齐'),
						Array('KIND_JUSTIFYFULL', 'justifyfull.gif', '两端对齐'),
						Array('KIND_NUMBEREDLIST', 'numberedlist.gif', '编号'),
						Array('KIND_UNORDERLIST', 'unorderedlist.gif', '项目符号'),
						Array('KIND_INDENT', 'indent.gif', '减少缩进'),
						Array('KIND_OUTDENT', 'outdent.gif', '增加缩进'),
						Array('KIND_SUBSCRIPT', 'subscript.gif', '下标'),
						Array('KIND_SUPERSCRIPT', 'superscript.gif', '上标'),
						Array('KIND_DATE', 'date.gif', '日期'),
						Array('KIND_TIME', 'time.gif', '时间')
				  );
var BOTTOM_TOOLBAR_ICON = Array(
						Array('KIND_TITLE', 'title.gif', '标题'),
						Array('KIND_FONTNAME', 'font.gif', '字体'),
						Array('KIND_FONTSIZE', 'fontsize.gif', '文字大小'),
						Array('KIND_TEXTCOLOR', 'textcolor.gif', '文字颜色'),
						Array('KIND_BGCOLOR', 'bgcolor.gif', '文字背景'),
						Array('KIND_BOLD', 'bold.gif', '粗体'),
						Array('KIND_ITALIC', 'italic.gif', '斜体'),
						Array('KIND_UNDERLINE', 'underline.gif', '下划线'),
						Array('KIND_STRIKE', 'strikethrough.gif', '删除线'),
						Array('KIND_REMOVE', 'removeformat.gif', '删除格式'),
						Array('KIND_IMAGE', 'image.gif', '图片'),
						Array('KIND_FLASH', 'flash.gif', 'Flash'),
						Array('KIND_MEDIA', 'media.gif', 'Windows Media Player'),
						Array('KIND_REAL', 'real.gif', 'Real Player'),
						Array('KIND_LAYER', 'layer.gif', '层'),
						Array('KIND_TABLE', 'table.gif', '表格'),
						Array('KIND_SPECIALCHAR', 'specialchar.gif', '特殊字符'),
						Array('KIND_HR', 'hr.gif', '横线'),
						Array('KIND_ICON', 'emoticons.gif', '笑脸'),
						Array('KIND_LINK', 'link.gif', '创建超级连接'),
						Array('KIND_UNLINK', 'unlink.gif', '删除超级连接'),
						Array('KIND_ABOUT', 'about.gif', '关于')
				  );
var SIMPLE_TOOLBAR_ICON = Array(
						Array('KIND_FONTNAME', 'font.gif', '字体'),
						Array('KIND_FONTSIZE', 'fontsize.gif', '文字大小'),
						Array('KIND_TEXTCOLOR', 'textcolor.gif', '文字颜色'),
						Array('KIND_BGCOLOR', 'bgcolor.gif', '文字背景'),
						Array('KIND_BOLD', 'bold.gif', '粗体'),
						Array('KIND_ITALIC', 'italic.gif', '斜体'),
						Array('KIND_UNDERLINE', 'underline.gif', '下划线'),
						Array('KIND_JUSTIFYLEFT', 'justifyleft.gif', '左对齐'),
						Array('KIND_JUSTIFYCENTER', 'justifycenter.gif', '居中'),
						Array('KIND_JUSTIFYRIGHT', 'justifyright.gif', '右对齐'),
						Array('KIND_IMAGE', 'image.gif', '图片'),
						Array('KIND_LAYER', 'layer.gif', '层'),
						Array('KIND_HR', 'hr.gif', '横线'),
						Array('KIND_ICON', 'emoticons.gif', '笑脸'),
						Array('KIND_LINK', 'link.gif', '创建超级连接'),
						Array('KIND_ABOUT', 'about.gif', '关于')
				  );
var POPUP_MENU_TABLE = Array("KIND_ZOOM", "KIND_TITLE", "KIND_FONTNAME", "KIND_FONTSIZE", "KIND_TEXTCOLOR", "KIND_BGCOLOR", 
							"KIND_LAYER", "KIND_TABLE", "KIND_HR", "KIND_ICON", "KIND_SPECIALCHAR", "KIND_ABOUT", 
							"KIND_IMAGE", "KIND_FLASH", "KIND_MEDIA", "KIND_REAL", "KIND_LINK");
var COLOR_TABLE = Array(
						"#FF0000", "#FFFF00", "#00FF00", "#00FFFF", "#0000FF", "#FF00FF", "#FFFFFF", "#F5F5F5", "#DCDCDC", "#FFFAFA",
						"#D3D3D3", "#C0C0C0", "#A9A9A9", "#808080", "#696969", "#000000", "#2F4F4F", "#708090", "#778899", "#4682B4",
						"#4169E1", "#6495ED", "#B0C4DE", "#7B68EE", "#6A5ACD", "#483D8B", "#191970", "#000080", "#00008B", "#0000CD",
						"#1E90FF", "#00BFFF", "#87CEFA", "#87CEEB", "#ADD8E6", "#B0E0E6", "#F0FFFF", "#E0FFFF", "#AFEEEE", "#00CED1",
						"#5F9EA0", "#48D1CC", "#00FFFF", "#40E0D0", "#20B2AA", "#008B8B", "#008080", "#7FFFD4", "#66CDAA", "#8FBC8F",
						"#3CB371", "#2E8B57", "#006400", "#008000", "#228B22", "#32CD32", "#00FF00", "#7FFF00", "#7CFC00", "#ADFF2F",
						"#98FB98", "#90EE90", "#00FF7F", "#00FA9A", "#556B2F", "#6B8E23", "#808000", "#BDB76B", "#B8860B", "#DAA520",
						"#FFD700", "#F0E68C", "#EEE8AA", "#FFEBCD", "#FFE4B5", "#F5DEB3", "#FFDEAD", "#DEB887", "#D2B48C", "#BC8F8F",
						"#A0522D", "#8B4513", "#D2691E", "#CD853F", "#F4A460", "#8B0000", "#800000", "#A52A2A", "#B22222", "#CD5C5C",
						"#F08080", "#FA8072", "#E9967A", "#FFA07A", "#FF7F50", "#FF6347", "#FF8C00", "#FFA500", "#FF4500", "#DC143C",
						"#FF0000", "#FF1493", "#FF00FF", "#FF69B4", "#FFB6C1", "#FFC0CB", "#DB7093", "#C71585", "#800080", "#8B008B",
						"#9370DB", "#8A2BE2", "#4B0082", "#9400D3", "#9932CC", "#BA55D3", "#DA70D6", "#EE82EE", "#DDA0DD", "#D8BFD8",
						"#E6E6FA", "#F8F8FF", "#F0F8FF", "#F5FFFA", "#F0FFF0", "#FAFAD2", "#FFFACD", "#FFF8DC", "#FFFFE0", "#FFFFF0",
						"#FFFAF0", "#FAF0E6", "#FDF5E6", "#FAEBD7", "#FFE4C4", "#FFDAB9", "#FFEFD5", "#FFF5EE", "#FFF0F5", "#FFE4E1"
					);
var IMAGE_ALIGN_TABLE = new Array("baseline", "top", "middle", "bottom", "texttop", "absmiddle", "absbottom", "left", "right");

function KindGetBrowser()
{
	var browser = '';
	var agentInfo = navigator.userAgent.toLowerCase();
	if (agentInfo.indexOf("msie") > -1) {
		var re = new RegExp("msie\\s?([\\d\\.]+)","ig");
		var arr = re.exec(agentInfo);
		if (parseInt(RegExp.$1) >= 5.5) {
			browser = 'IE';
		}
	} else if (agentInfo.indexOf("firefox") > -1) {
		browser = 'FF';
	} else if (agentInfo.indexOf("netscape") > -1) {
		var temp1 = agentInfo.split(' ');
		var temp2 = temp1[temp1.length-1].split('/');
		if (parseInt(temp2[1]) >= 7) {
			browser = 'NS';
		}
	} else if (agentInfo.indexOf("gecko") > -1) {
		browser = 'ML';
	} else if (agentInfo.indexOf("opera") > -1) {
		var temp1 = agentInfo.split(' ');
		var temp2 = temp1[0].split('/');
		if (parseInt(temp2[1]) >= 9) {
			browser = 'OPERA';
		}
	}
	return browser;
}
function KindGetFileName(file, separator)
{
	var temp = file.split(separator);
	var len = temp.length;
	var fileName = temp[len-1];
	return fileName;
}
function KindGetFileExt(fileName)
{
	var temp = fileName.split(".");
	var len = temp.length;
	var fileExt = temp[len-1].toLowerCase();
	return fileExt;
}
function KindCheckImageFileType(file, separator)
{
	if (separator == "/" && file.match(/http:\/\/.{3,}/) == null) {
		alert(MSG_INPUT_URL);
		return false;
	}
	var fileName = KindGetFileName(file, separator);
	var fileExt = KindGetFileExt(fileName);
	if (fileExt != 'gif' && fileExt != 'jpg' && fileExt != 'png' && fileExt != 'bmp') {
		alert(MSG_INVALID_IMAGE);
		return false;
	}
	return true;
}
function KindCheckFlashFileType(file, separator)
{
	if (file.match(/http:\/\/.{3,}/) == null) {
		alert(MSG_INPUT_URL);
		return false;
	}
	var fileName = KindGetFileName(file, "/");
	var fileExt = KindGetFileExt(fileName);
	if (fileExt != 'swf') {
		alert(MSG_INVALID_FLASH);
		return false;
	}
	return true;
}
function KindCheckMediaFileType(cmd, file, separator)
{
	if (file.match(/http:\/\/.{3,}/) == null) {
		alert(MSG_INPUT_URL);
		return false;
	}
	var fileName = KindGetFileName(file, "/");
	var fileExt = KindGetFileExt(fileName);
	if (cmd == 'KIND_REAL') {
		if (fileExt != 'rm' && fileExt != 'rmvb') {
			alert(MSG_INVALID_REAL);
			return false;
		}
	} else {
		if (fileExt != 'mp3' && fileExt != 'wav' && fileExt != 'wma' && fileExt != 'wmv' && fileExt != 'mid' && fileExt != 'avi' && fileExt != 'mpg' && fileExt != 'asf') {
			alert(MSG_INVALID_MEDIA);
			return false;
		}
	}
	return true;
}
function KindImageToObj(str)
{
	str = str.replace(/<img([^>]*id="flashpreviewimg"[^>]*)>/gi, function ($0,$1) {
					var width = $1.match(/width:\s?(\d+)/i);
					var height = $1.match(/height:\s?(\d+)/i);
					var url = $1.match(/alt="([^"\s>]+)"/i);
					var ret = KindGetFlashHtmlTag(url[1], width[1], height[1]);
					return ret;
				}
			);
	str = str.replace(/<img([^>]*id="mediapreviewimg"[^>]*)>/gi, function ($0,$1) {
					var width = $1.match(/width:\s?(\d+)/i);
					var height = $1.match(/height:\s?(\d+)/i);
					var url = $1.match(/alt="([^"\s>]+)"/i);
					var title = $1.match(/title="([^"\s>]+)"/i);
					var ret = KindGetMediaHtmlTag('KIND_MEDIA', url[1], width[1], height[1], title[1]);
					return ret;
				}
			);
	str = str.replace(/<img([^>]*id="realpreviewimg"[^>]*)>/gi, function ($0,$1) {
					var width = $1.match(/width:\s?(\d+)/i);
					var height = $1.match(/height:\s?(\d+)/i);
					var url = $1.match(/alt="([^"\s>]+)"/i);
					var title = $1.match(/title="([^"\s>]+)"/i);
					var ret = KindGetMediaHtmlTag('KIND_REAL', url[1], width[1], height[1], title[1]);
					return ret;
				}
			);
	return str;
}
function KindObjToImage(str)
{
	str = str.replace(/<object([^>]*type="application\/x-shockwave-flash"[^>]*)>.*?<\/object>/gi, function ($0,$1) {
					var url = IMAGE_PATH + 'flashpreview.gif';
					var width = $1.match(/width="([^"\s>]+)"/i);
					var height = $1.match(/height="([^"\s>]+)"/i);
					var alt = $1.match(/data="([^"\s>]+)"/i);
					var ret = '<img id="flashpreviewimg" src="'+url+'" style="' + 
							'width:'+width[1]+'px;height:'+height[1]+'px;" alt="'+alt[1]+'" border="0">';
					return ret;
				}
			);
	str = str.replace(/<object([^>]*type="video\/x-ms-asf-plugin"[^>]*)>(.*?)<\/object>/gi, function ($0,$1,$2) {
					var url = IMAGE_PATH + 'mediapreview.gif';
					var width = $1.match(/width="([^"\s>]+)"/i);
					var height = $1.match(/height="([^"\s>]+)"/i);
					var alt = $1.match(/data="([^"\s>]+)"/i);
					var autostart = $2.match(/name="autostart" value="(\w+)"/i);
					var controls = $2.match(/name="EnableContextMenu" value="(\w+)"/i);
					var title = autostart[1] + '-' + controls[1];
					var ret = '<img id="mediapreviewimg" src="'+url+'" style="' + 
							'width:'+width[1]+'px;height:'+height[1]+'px;" alt="'+alt[1]+'" title="'+title+'" border="0">';
					return ret;
				}
			);
	str = str.replace(/<object([^>]*type="audio\/x-pn-realaudio-plugin"[^>]*)>(.*?)<\/object>/gi, function ($0,$1,$2) {
					var url = IMAGE_PATH + 'realpreview.gif';
					var width = $1.match(/width="([^"\s>]+)"/i);
					var height = $1.match(/height="([^"\s>]+)"/i);
					var alt = $1.match(/data="([^"\s>]+)"/i);
					var autostart = $2.match(/name="autostart" value="(\w+)"/i);
					var controls = $2.match(/name="controls" value="([\w,]+)"/i);
					var title = autostart[1] + '-' + controls[1];
					var ret = '<img id="realpreviewimg" src="'+url+'" style="' + 
							'width:'+width[1]+'px;height:'+height[1]+'px;" alt="'+alt[1]+'" title="'+title+'" border="0">';
					return ret;
				}
			);
	return str;

}
function KindHtmlToXhtml(str) 
{
	str = str.replace(/<p(.*?>)/gi, "<div$1");
	str = str.replace(/<\/p>/gi, "</div>");
	str = str.replace(/<br.*?>/gi, "<br />");
	str = str.replace(/(<hr[^>]*[^\/])(>)/gi, "$1 />");
	str = str.replace(/(<img[^>]*[^\/])(>)/gi, "$1 />");
	str = str.replace(/(<\w+)(.*?>)/gi, function ($0,$1,$2) {
						return($1.toLowerCase() + KindConvertAttribute($2));
					}
				);
	str = str.replace(/(<\/\w+>)/gi, function ($0,$1) {
						return($1.toLowerCase());
					}
				);
	str = KindTrim(str);
	return str;
}
function KindConvertAttribute(str)
{
	str = KindConvertAttributeChild(str, 'style', '[^\"\'>]+');
	str = KindConvertAttributeChild(str, 'src', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'href', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'color', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'alt', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'title', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'type', '[^\"\'\\s>]+');
	str = KindConvertAttributeChild(str, 'face', '[^\"\'>]+');
	str = KindConvertAttributeChild(str, 'id', '\\w+');
	str = KindConvertAttributeChild(str, 'name', '\\w+');
	str = KindConvertAttributeChild(str, 'dir', '\\w+');
	str = KindConvertAttributeChild(str, 'target', '\\w+');
	str = KindConvertAttributeChild(str, 'align', '\\w+');
	str = KindConvertAttributeChild(str, 'width', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'height', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'border', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'hspace', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'vspace', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'size', '[\\w%]+');
	str = KindConvertAttributeChild(str, 'cellspacing', '\\d+');
	str = KindConvertAttributeChild(str, 'cellpadding', '\\d+');
	if (SAFE_MODE == true) {
		str = KindClearAttributeScriptTag(str);
	}
	return str;
}
function KindConvertAttributeChild(str, attName, regStr)
{
	var re = new RegExp("("+attName+"=)[\"']?("+regStr+")[\"']?", "ig");
	var reUrl = new RegExp("http://" + SITE_DOMAIN + "(/.*)", "i");
	str = str.replace(re, function ($0,$1,$2) {
						var val = $2;
						if (val.match(reUrl) != null) {
							val = val.replace(reUrl, "$1");
						}
						if (BROWSER == 'IE' && attName.match(/style/i) != null) {
							return($1.toLowerCase() + "\"" + val.toLowerCase() + "\"");
						} else {
							return($1.toLowerCase() + "\"" + val + "\"");
						}
					}
				);
	return str;
}
function KindClearAttributeScriptTag(str)
{
	var re = new RegExp("(\\son[a-z]+=)[\"']?[^>]*?[^\\\\\>][\"']?([\\s>])","ig");
	str = str.replace(re, function ($0,$1,$2) {
						return($1.toLowerCase() + "\"\"" + $2);
					}
				);
	return str;
}
function KindClearScriptTag(str)
{
	if (SAFE_MODE == false) {
		return str;
	}
	str = str.replace(/<(script.*?)>/gi, "[$1]");
	str = str.replace(/<\/script>/gi, "[/script]");
	return str;
}
function KindTrim(str)
{
	str = str.replace(/^\s+|\s+$/g, "");
	str = str.replace(/[\r\n]+/g, "\r\n");
	return str;
}
function KindHtmlentities(str)
{
	str = str.replace(/&/g,'&amp;');
	str = str.replace(/</g,'&lt;');
	str = str.replace(/>/g,'&gt;');
	str = str.replace(/"/g,'&quot;');
	return str;
}
function KindHtmlentitiesDecode(str)
{
	str = str.replace(/&lt;/g,'<');
	str = str.replace(/&gt;/g,'>');
	str = str.replace(/&quot;/g,'"');
	str = str.replace(/&amp;/g,'&');
	return str;
}
function KindGetTop(id)
{
	var top = 28;
	var tmp = '';
	var obj = document.getElementById(id);
	while (eval("obj" + tmp).tagName != "BODY") {
		tmp += ".offsetParent";
		top += eval("obj" + tmp).offsetTop;
	}
	return top;
}
function KindGetLeft(id)
{
	var left = 2;
	var tmp = '';
	var obj = document.getElementById(id);
	while (eval("obj" + tmp).tagName != "BODY") {
		tmp += ".offsetParent";
		left += eval("obj" + tmp).offsetLeft;
	}
	return left;
}
function KindDisplayMenu(cmd)
{
	if (cmd != 'KIND_ABOUT') {
		KindEditorForm.focus();
		KindSelection();
	}
	KindDisableMenu();
	var top, left;
	top = KindGetTop(cmd);
	left = KindGetLeft(cmd);
	if (cmd == 'KIND_ABOUT') {
		left -= 168;
	} else if (cmd == 'KIND_LINK') {
		left -= 220;
	}
	document.getElementById('POPUP_'+cmd).style.top =  top.toString(10) + 'px';
	document.getElementById('POPUP_'+cmd).style.left = left.toString(10) + 'px';
	document.getElementById('POPUP_'+cmd).style.display = 'block';
}
function KindDisableMenu()
{
	for (i = 0; i < POPUP_MENU_TABLE.length; i++) {
		document.getElementById('POPUP_'+POPUP_MENU_TABLE[i]).style.display = 'none';
	}
}
function KindReloadIframe()
{
	var str = '';
	str += KindPopupMenu('KIND_IMAGE');
	str += KindPopupMenu('KIND_FLASH');
	str += KindPopupMenu('KIND_MEDIA');
	str += KindPopupMenu('KIND_REAL');
	document.getElementById('InsertIframe').innerHTML = str;
	KindDrawIframe('KIND_IMAGE');
	KindDrawIframe('KIND_FLASH');
	KindDrawIframe('KIND_MEDIA');
	KindDrawIframe('KIND_REAL');
}
function KindGetMenuCommonStyle()
{
	var str = 'position:absolute;top:1px;left:1px;font-size:12px;color:'+MENU_TEXT_COLOR+
			';background-color:'+MENU_BG_COLOR+';border:solid 1px '+MENU_BORDER_COLOR+';z-index:1;display:none;';
	return str;
}
function KindGetCommonMenu(cmd, content)
{
	var str = '';
	str += '<div id="POPUP_'+cmd+'" style="'+KindGetMenuCommonStyle()+'">';
	str += content;
	str += '</div>';
	return str;
}
function KindCreateColorTable(cmd, eventStr)
{
	var str = '';
	str += '<table cellpadding="0" cellspacing="2" border="0">';
	for (i = 0; i < COLOR_TABLE.length; i++) {
		if (i == 0 || (i >= 10 && i%10 == 0)) {
			str += '<tr>';
		}
		str += '<td style="width:12px;height:12px;border:1px solid #AAAAAA;font-size:1px;cursor:pointer;background-color:' +
		COLOR_TABLE[i] + ';" onmouseover="javascript:this.style.borderColor=\'#000000\';' + ((eventStr) ? eventStr : '') + '" ' +
		'onmouseout="javascript:this.style.borderColor=\'#AAAAAA\';" ' + 
		'onclick="javascript:KindExecute(\''+cmd+'_END\', \'' + COLOR_TABLE[i] + '\');">&nbsp;</td>';
		if (i >= 9 && i%(i-1) == 0) {
			str += '</tr>';
		}
	}
	str += '</table>';
	return str;
}
function KindDrawColorTable(cmd)
{
	var str = '';
	str += '<div id="POPUP_'+cmd+'" style="width:160px;padding:2px;'+KindGetMenuCommonStyle()+'">';
	str += KindCreateColorTable(cmd);
	str += '</div>';
	return str;
}
function KindDrawMedia(cmd)
{
	var str = '';
	str += '<table cellpadding="0" cellspacing="0" style="width:100%">' + 
		'<tr><td colspan="2"><table border="0"><tr><td id="'+cmd+'preview" style="width:240px;height:240px;border:1px solid #AAAAAA;background-color:#FFFFFF;" align="center" valign="middle">&nbsp;</td></tr></table></td></tr>' +  	
		'<tr><td style="width:40px;padding:5px;">'+STR_IMAGE_REMOTE+'</td>' +
		'<td style="width:210px;padding-bottom:5px;"><input type="text" id="'+cmd+'link" value="http://" style="width:190px;border:1px solid #555555;" /></td></tr>' +
		'<tr><td colspan="2" style="margin:5px;padding-bottom:5px;" align="center">' +
		'<input type="button" name="button" value="'+STR_BUTTON_LISTENING+'" onclick="javascript:parent.KindMediaPreview(\''+cmd+'\');" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
		'<input type="submit" name="button" id="'+cmd+'submitButton" value="'+STR_BUTTON_CONFIRM+'" onclick="javascript:parent.KindDrawMediaEnd(\''+cmd+'\');" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
		'<input type="button" name="button" value="'+STR_BUTTON_CANCEL+'" onclick="javascript:parent.KindDisableMenu();parent.KindReloadIframe();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /></td></tr>' + 
		'</table>';
	return str;
}
function KindPopupMenu(cmd)
{
	switch (cmd)
	{
		case 'KIND_ZOOM':
			var str = '';
			for (i = 0; i < ZOOM_TABLE.length; i++) {
				str += '<div style="padding:2px;width:120px;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_ZOOM_END\', \'' + ZOOM_TABLE[i] + '\');" ' + 
				'onmouseover="javascript:this.style.backgroundColor=\''+MENU_SELECTED_COLOR+'\';" ' +
				'onmouseout="javascript:this.style.backgroundColor=\''+MENU_BG_COLOR+'\';">' + 
				ZOOM_TABLE[i] + '</div>';
			}
			str = KindGetCommonMenu('KIND_ZOOM', str);
			return str;
			break;
		case 'KIND_TITLE':
			var str = '';
			for (i = 0; i < TITLE_TABLE.length; i++) {
				str += '<div style="width:140px;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_TITLE_END\', \'' + TITLE_TABLE[i][0] + '\');" ' + 
				'onmouseover="javascript:this.style.backgroundColor=\''+MENU_SELECTED_COLOR+'\';" ' +
				'onmouseout="javascript:this.style.backgroundColor=\''+MENU_BG_COLOR+'\';"><' + TITLE_TABLE[i][0] + ' style="margin:2px;">' + 
				TITLE_TABLE[i][1] + '</' + TITLE_TABLE[i][0] + '></div>';
			}
			str = KindGetCommonMenu('KIND_TITLE', str);
			return str;
			break;
		case 'KIND_FONTNAME':
			var str = '';
			for (i = 0; i < FONT_NAME.length; i++) {
				str += '<div style="font-family:' + FONT_NAME[i][0] + 
				';padding:2px;width:160px;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_FONTNAME_END\', \'' + FONT_NAME[i][0] + '\');" ' + 
				'onmouseover="javascript:this.style.backgroundColor=\''+MENU_SELECTED_COLOR+'\';" ' +
				'onmouseout="javascript:this.style.backgroundColor=\''+MENU_BG_COLOR+'\';">' + 
				FONT_NAME[i][1] + '</div>';
			}
			str = KindGetCommonMenu('KIND_FONTNAME', str);
			return str;
			break;
		case 'KIND_FONTSIZE':
			var str = '';
			for (i = 0; i < FONT_SIZE.length; i++) {
				str += '<div style="font-size:' + FONT_SIZE[i][1] + 
				';padding:2px;width:120px;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_FONTSIZE_END\', \'' + FONT_SIZE[i][0] + '\');" ' + 
				'onmouseover="javascript:this.style.backgroundColor=\''+MENU_SELECTED_COLOR+'\';" ' +
				'onmouseout="javascript:this.style.backgroundColor=\''+MENU_BG_COLOR+'\';">' + 
				FONT_SIZE[i][1] + '</div>';
			}
			str = KindGetCommonMenu('KIND_FONTSIZE', str);
			return str;
			break;
		case 'KIND_TEXTCOLOR':
			var str = '';
			str = KindDrawColorTable('KIND_TEXTCOLOR');
			return str;
			break;
		case 'KIND_BGCOLOR':
			var str = '';
			str = KindDrawColorTable('KIND_BGCOLOR');
			return str;
			break;
		case 'KIND_HR':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:160px;'+KindGetMenuCommonStyle()+'">';
			str += '<div id="hrPreview" style="margin:10px 2px 10px 2px;height:1px;border:0;font-size:0;background-color:#FFFFFF;"></div>';
			str += KindCreateColorTable(cmd, 'document.getElementById(\'hrPreview\').style.backgroundColor = this.style.backgroundColor;');
			str += '</div>';
			return str;
			break;
		case 'KIND_LAYER':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:160px;'+KindGetMenuCommonStyle()+'">';
			str += '<div id="divPreview" style="margin:5px 2px 5px 2px;height:20px;border:1px solid #AAAAAA;font-size:1px;background-color:#FFFFFF;"></div>';
			str += KindCreateColorTable(cmd, 'document.getElementById(\'divPreview\').style.backgroundColor = this.style.backgroundColor;');
			str += '</div>';
			return str;
			break;
		case 'KIND_ICON':
			var str = '';
			var iconNum = 36;
			str += '<table id="POPUP_'+cmd+'" cellpadding="0" cellspacing="2" style="'+KindGetMenuCommonStyle()+'">';
			for (i = 0; i < iconNum; i++) {
				if (i == 0 || (i >= 6 && i%6 == 0)) {
					str += '<tr>';
				}
				var num;
				if ((i+1).toString(10).length < 2) {
					num = '0' + (i+1);
				} else {
					num = (i+1).toString(10);
				}
				var iconUrl = ICON_PATH + 'etc_' + num + '.gif';
				str += '<td style="padding:2px;border:0;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_ICON_END\', \'' + iconUrl + '\');">' +
				'<img src="' + iconUrl + '" style="border:1px solid #EEEEEE;" onmouseover="javascript:this.style.borderColor=\'#AAAAAA\';" ' +
				'onmouseout="javascript:this.style.borderColor=\'#EEEEEE\';">' + '</td>';
				if (i >= 5 && i%(i-1) == 0) {
					str += '</tr>';
				}
			}
			str += '</table>';
			return str;
			break;
		case 'KIND_SPECIALCHAR':
			var str = '';
			str += '<table id="POPUP_'+cmd+'" cellpadding="0" cellspacing="2" style="'+KindGetMenuCommonStyle()+'">';
			for (i = 0; i < SPECIAL_CHARACTER.length; i++) {
				if (i == 0 || (i >= 10 && i%10 == 0)) {
					str += '<tr>';
				}
				str += '<td style="padding:2px;border:1px solid #AAAAAA;cursor:pointer;" ' + 
				'onclick="javascript:KindExecute(\'KIND_SPECIALCHAR_END\', \'' + SPECIAL_CHARACTER[i] + '\');" ' +
				'onmouseover="javascript:this.style.borderColor=\'#000000\';" ' +
				'onmouseout="javascript:this.style.borderColor=\'#AAAAAA\';">' + SPECIAL_CHARACTER[i] + '</td>';
				if (i >= 9 && i%(i-1) == 0) {
					str += '</tr>';
				}
			}
			str += '</table>';
			return str;
			break;
		case 'KIND_TABLE':
			var str = '';
			var num = 10;
			str += '<table id="POPUP_'+cmd+'" cellpadding="0" cellspacing="0" style="'+KindGetMenuCommonStyle()+'">';
			for (i = 1; i <= num; i++) {
				str += '<tr>';
				for (j = 1; j <= num; j++) {
					var value = i.toString(10) + ',' + j.toString(10);
					str += '<td id="kindTableTd' + i.toString(10) + '_' + j.toString(10) + 
					'" style="width:15px;height:15px;background-color:#FFFFFF;border:1px solid #DDDDDD;cursor:pointer;" ' + 
					'onclick="javascript:KindExecute(\'KIND_TABLE_END\', \'' + value + '\');" ' +
					'onmouseover="javascript:KindDrawTableSelected(\''+i.toString(10)+'\', \''+j.toString(10)+'\');" ' + 
					'onmouseout="javascript:;">&nbsp;</td>';
				}
				str += '</tr>';
			}
			str += '<tr><td colspan="10" id="tableLocation" style="text-align:center;height:20px;"></td></tr>';
			str += '</table>';
			return str;
			break;
		case 'KIND_IMAGE':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:250px;'+KindGetMenuCommonStyle()+'">';
			str += '<iframe name="KindImageIframe" id="KindImageIframe" frameborder="0" style="width:250px;height:390px;padding:0;margin:0;border:0;">';
			str += '</iframe></div>';
			return str;
			break;
		case 'KIND_FLASH':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:250px;'+KindGetMenuCommonStyle()+'">';
			str += '<iframe name="KindFlashIframe" id="KindFlashIframe" frameborder="0" style="width:250px;height:300px;padding:0;margin:0;border:0;">';
			str += '</iframe></div>';
			return str;
			break;
		case 'KIND_MEDIA':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:250px;'+KindGetMenuCommonStyle()+'">';
			str += '<iframe name="KindMediaIframe" id="KindMediaIframe" frameborder="0" style="width:250px;height:300px;padding:0;margin:0;border:0;">';
			str += '</iframe></div>';
			return str;
			break;
		case 'KIND_REAL':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:250px;'+KindGetMenuCommonStyle()+'">';
			str += '<iframe name="KindRealIframe" id="KindRealIframe" frameborder="0" style="width:250px;height:300px;padding:0;margin:0;border:0;">';
			str += '</iframe></div>';
			return str;
			break;
		case 'KIND_LINK':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:250px;'+KindGetMenuCommonStyle()+'">';
			str += '<iframe name="KindLinkIframe" id="KindLinkIframe" frameborder="0" style="width:250px;height:85px;padding:0;margin:0;border:0;">';
			str += '</iframe></div>';
			return str;
			break;
		case 'KIND_ABOUT':
			var str = '';
			str += '<div id="POPUP_'+cmd+'" style="width:200px;'+KindGetMenuCommonStyle()+';padding:5px;">';
			str += '<span style="margin-right:10px;">KindEditor 2.4</span>' + 
				'<a href="http://www.kindsoft.net/" target="_blank" style="color:#4169e1;" onclick="javascript:KindDisableMenu();">'+STR_ABOUT+'</a><br />';
			str += '</div>';
			return str;
			break;
		default: 
			break;
	}
}
function KindDrawIframe(cmd)
{
	if (BROWSER == 'IE') {
		IMAGE_DOCUMENT = document.frames("KindImageIframe").document;
		FLASH_DOCUMENT = document.frames("KindFlashIframe").document;
		MEDIA_DOCUMENT = document.frames("KindMediaIframe").document;
		REAL_DOCUMENT = document.frames("KindRealIframe").document;
		LINK_DOCUMENT = document.frames("KindLinkIframe").document;
	} else {
		IMAGE_DOCUMENT = document.getElementById('KindImageIframe').contentDocument;
		FLASH_DOCUMENT = document.getElementById('KindFlashIframe').contentDocument;
		MEDIA_DOCUMENT = document.getElementById('KindMediaIframe').contentDocument;
		REAL_DOCUMENT = document.getElementById('KindRealIframe').contentDocument;
		LINK_DOCUMENT = document.getElementById('KindLinkIframe').contentDocument;
	}
	switch (cmd)
	{
		case 'KIND_IMAGE':
			var str = '';
			str += '<div align="center">' +
				'<form name="uploadForm" style="margin:0;padding:0;" method="post" enctype="multipart/form-data" ' +
				'action="' + IMAGE_UPLOAD_CGI + '" onsubmit="javascript:if(parent.KindDrawImageEnd()==false){return false;};">' +
				'<input type="hidden" name="fileName" id="fileName" value="" />' + 
				'<table cellpadding="0" cellspacing="0" style="width:100%;font-size:12px;">' + 
				'<tr><td colspan="2"><table border="0" style="margin-bottom:3px;"><tr><td id="imgPreview" style="width:240px;height:240px;border:1px solid #AAAAAA;background-color:#FFFFFF;" align="center" valign="middle">&nbsp;</td></tr></table></td></tr>' +  	
				'<tr><td style="width:50px;padding-left:5px;">';
			if (UPLOAD_MODE == true) {
				str += '<select id="imageType" onchange="javascript:parent.KindImageType(this.value);document.getElementById(\''+cmd+'submitButton\').focus();"><option value="1" selected="selected">'+STR_IMAGE_LOCAL+'</option><option value="2">'+STR_IMAGE_REMOTE+'</option></select>';
			} else {
				str += STR_IMAGE_REMOTE;
			}
			str += '</td><td style="width:200px;padding-bottom:3px;">';
			if (UPLOAD_MODE == true) {
				str += '<input type="text" id="imgLink" value="http://" maxlength="255" style="width:95%;border:1px solid #555555;display:none;" />' +
				'<input type="file" name="fileData" id="imgFile" size="14" style="border:1px solid #555555;" onclick="javascript:document.getElementById(\'imgLink\').value=\'http://\';" />';
			} else {
				str += '<input type="text" id="imgLink" value="http://" maxlength="255" style="width:95%;border:1px solid #555555;" />' +
				'<input type="hidden" name="imageType" id="imageType" value="2"><input type="hidden" name="fileData" id="imgFile" value="" />';
			}
			str += '</td></tr><tr><td colspan="2" style="padding-bottom:3px;">' +
				'<table border="0" style="width:100%;font-size:12px;"><tr>' +
				'<td width="18%" style="padding:2px 2px 2px 5px;">'+STR_TITLE+'</td><td width="82%"><input type="text" name="imgTitle" id="imgTitle" value="" maxlength="100" style="width:95%;border:1px solid #555555;" /></td></tr></table>' +	
				'<table border="0" style="width:100%;font-size:12px;"><tr>' +
				'<td width="10%" style="padding:2px 2px 2px 5px;">'+STR_WIDTH+'</td><td width="23%"><input type="text" name="imgWidth" id="imgWidth" value="0" maxlength="4" style="width:40px;border:1px solid #555555;" /></td>' +
				'<td width="10%" style="padding:2px;">'+STR_HEIGHT+'</td><td width="23%"><input type="text" name="imgHeight" id="imgHeight" value="0" maxlength="4" style="width:40px;border:1px solid #555555;" /></td>' +
				'<td width="10%" style="padding:2px;">'+STR_BORDER+'</td><td width="23%"><input type="text" name="imgBorder" id="imgBorder" value="0" maxlength="1" style="width:20px;border:1px solid #555555;" /></td></tr></table>' +
				'<table border="0" style="width:100%;font-size:12px;"><tr>' +
				'<td width="39%" style="padding:2px 2px 2px 5px;"><select id="imgAlign" name="imgAlign"><option value="">'+STR_ALIGN+'</option>';
			for (var i = 0; i < IMAGE_ALIGN_TABLE.length; i++) {
				str += '<option value="' + IMAGE_ALIGN_TABLE[i] + '">' + IMAGE_ALIGN_TABLE[i] + '</option>';
			}
			str += '</select></td>' +
				'<td width="15%" style="padding:2px;">'+STR_HSPACE+'</td><td width="15%"><input type="text" name="imgHspace" id="imgHspace" value="0" maxlength="1" style="width:20px;border:1px solid #555555;" /></td>' +
				'<td width="15%" style="padding:2px;">'+STR_VSPACE+'</td><td width="15%"><input type="text" name="imgVspace" id="imgVspace" value="0" maxlength="1" style="width:20px;border:1px solid #555555;" /></td></tr></table>' +
				'</td></tr><tr><td colspan="2" style="margin:5px;padding-bottom:5px;" align="center">' +
				'<input type="button" name="button" value="'+STR_BUTTON_PREVIEW+'" onclick="javascript:parent.KindImagePreview();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
				'<input type="submit" name="button" id="'+cmd+'submitButton" value="'+STR_BUTTON_CONFIRM+'" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
				'<input type="button" name="button" value="'+STR_BUTTON_CANCEL+'" onclick="javascript:parent.KindDisableMenu();parent.KindReloadIframe();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /></td></tr>' + 
				'</table></form></div>';
			KindWriteFullHtml(IMAGE_DOCUMENT, str);
			IMAGE_DOCUMENT.body.style.color = MENU_TEXT_COLOR;
			IMAGE_DOCUMENT.body.style.backgroundColor = MENU_BG_COLOR;
			IMAGE_DOCUMENT.body.style.margin = 0;
			IMAGE_DOCUMENT.body.scroll = 'no';
			break;
		case 'KIND_FLASH':
			var str = '';
			str += '<table cellpadding="0" cellspacing="0" style="width:100%;">' + 
			'<tr><td colspan="2"><table border="0"><tr><td id="flashPreview" style="width:240px;height:240px;border:1px solid #AAAAAA;background-color:#FFFFFF;" align="center" valign="middle">&nbsp;</td></tr></table></td></tr>' +  	
			'<tr><td style="width:40px;padding:5px;">'+STR_IMAGE_REMOTE+'</td>' +
			'<td style="width:210px;padding-bottom:5px;"><input type="text" id="flashLink" value="http://" style="width:190px;border:1px solid #555555;" /></td></tr>' +
			'<tr><td colspan="2" style="margin:5px;padding-bottom:5px;" align="center">' +
			'<input type="button" name="button" value="'+STR_BUTTON_PREVIEW+'" onclick="javascript:parent.KindFlashPreview();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
			'<input type="submit" name="button" id="'+cmd+'submitButton" value="'+STR_BUTTON_CONFIRM+'" onclick="javascript:parent.KindDrawFlashEnd();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
			'<input type="button" name="button" value="'+STR_BUTTON_CANCEL+'" onclick="javascript:parent.KindDisableMenu();parent.KindReloadIframe();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /></td></tr>' + 
			'</table>';
			KindWriteFullHtml(FLASH_DOCUMENT, str);
			FLASH_DOCUMENT.body.style.color = MENU_TEXT_COLOR;
			FLASH_DOCUMENT.body.style.backgroundColor = MENU_BG_COLOR;
			FLASH_DOCUMENT.body.style.margin = 0;
			FLASH_DOCUMENT.body.scroll = 'no';
			break;
		case 'KIND_MEDIA':
			var str = '';
			str += KindDrawMedia(cmd);
			KindWriteFullHtml(MEDIA_DOCUMENT, str);
			MEDIA_DOCUMENT.body.style.color = MENU_TEXT_COLOR;
			MEDIA_DOCUMENT.body.style.backgroundColor = MENU_BG_COLOR;
			MEDIA_DOCUMENT.body.style.margin = 0;
			MEDIA_DOCUMENT.body.scroll = 'no';
			break;
		case 'KIND_REAL':
			var str = '';
			str += KindDrawMedia(cmd);
			KindWriteFullHtml(REAL_DOCUMENT, str);
			REAL_DOCUMENT.body.style.color = MENU_TEXT_COLOR;
			REAL_DOCUMENT.body.style.backgroundColor = MENU_BG_COLOR;
			REAL_DOCUMENT.body.style.margin = 0;
			REAL_DOCUMENT.body.scroll = 'no';
			break;
		case 'KIND_LINK':
			var str = '';
			str += '<table cellpadding="0" cellspacing="0" style="width:100%">' + 
				'<tr><td style="width:50px;padding:5px;">URL</td>' +
				'<td style="width:200px;padding-top:5px;padding-bottom:5px;"><input type="text" id="hyperLink" value="http://" style="width:190px;border:1px solid #555555;background-color:#FFFFFF;"></td>' +
				'<tr><td style="padding:5px;">'+STR_LINK_TARGET+'</td>' +
				'<td style="padding-bottom:5px;"><select id="hyperLinkTarget"><option value="_blank" selected="selected">'+STR_LINK_BLANK+'</option><option value="">'+STR_LINK_NOBLANK+'</option></select></td></tr>' + 
				'<tr><td colspan="2" style="padding-bottom:5px;" align="center">' +
				'<input type="submit" name="button" id="'+cmd+'submitButton" value="'+STR_BUTTON_CONFIRM+'" onclick="javascript:parent.KindDrawLinkEnd();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /> ' +
				'<input type="button" name="button" value="'+STR_BUTTON_CANCEL+'" onclick="javascript:parent.KindDisableMenu();" style="border:1px solid #555555;background-color:'+BUTTON_COLOR+';" /></td></tr>';
			str += '</table>';
			KindWriteFullHtml(LINK_DOCUMENT, str);
			LINK_DOCUMENT.body.style.color = MENU_TEXT_COLOR;
			LINK_DOCUMENT.body.style.backgroundColor = MENU_BG_COLOR;
			LINK_DOCUMENT.body.style.margin = 0;
			LINK_DOCUMENT.body.scroll = 'no';
			break;
		default:
			break;
	}
}
function KindDrawTableSelected(i, j)
{
	var text = i.toString(10) + ' by ' + j.toString(10) + ' Table';
	document.getElementById('tableLocation').innerHTML = text;
	var num = 10;
	for (m = 1; m <= num; m++) {
		for (n = 1; n <= num; n++) {
			var obj = document.getElementById('kindTableTd' + m.toString(10) + '_' + n.toString(10) + '');
			if (m <= i && n <= j) {
				obj.style.backgroundColor = MENU_SELECTED_COLOR;
			} else {
				obj.style.backgroundColor = '#FFFFFF';
			}
		}
	}
}
function KindImageType(type)
{
	if (type == 1) {
		IMAGE_DOCUMENT.getElementById('imgFile').style.display = 'block';
		IMAGE_DOCUMENT.getElementById('imgLink').style.display = 'none';
		IMAGE_DOCUMENT.getElementById('imgLink').value = 'http://';
	} else {
		IMAGE_DOCUMENT.getElementById('imgFile').style.display = 'none';
		IMAGE_DOCUMENT.getElementById('imgLink').style.display = 'block';
	}
	IMAGE_DOCUMENT.getElementById('imgPreview').innerHTML = "&nbsp;";
	IMAGE_DOCUMENT.getElementById('imgWidth').value = 0;
	IMAGE_DOCUMENT.getElementById('imgHeight').value = 0;
}
function KindImagePreview()
{
	var type = IMAGE_DOCUMENT.getElementById('imageType').value;
	var url = IMAGE_DOCUMENT.getElementById('imgLink').value;
	var file = IMAGE_DOCUMENT.getElementById('imgFile').value;
	if (type == 1) {
		if (BROWSER != 'IE') {
			return false;
		}
		if (file == '') {
			alert(MSG_SELECT_IMAGE);
			return false;
		}
		url = 'file:///' + file;
		if (KindCheckImageFileType(url, "\\") == false) {
			return false;
		}
	} else {
		if (KindCheckImageFileType(url, "/") == false) {
			return false;
		}
	}
	var imgObj = IMAGE_DOCUMENT.createElement("IMG");
	imgObj.src = url;
	var width = parseInt(imgObj.width);
	var height = parseInt(imgObj.height);
	IMAGE_DOCUMENT.getElementById('imgWidth').value = width;
	IMAGE_DOCUMENT.getElementById('imgHeight').value = height;
	var rate = parseInt(width/height);
	if (width >230 && height <= 230) {
		width = 230;
		height = parseInt(width/rate);
	} else if (width <=230 && height > 230) {
		height = 230;
		width = parseInt(height*rate);
	} else if (width >230 && height > 230) {
		if (width >= height) {
			width = 230;
			height = parseInt(width/rate);
		} else {
			height = 230;
			width = parseInt(height*rate);
		}
	}
	imgObj.style.width = width;
	imgObj.style.height = height;
	var el = IMAGE_DOCUMENT.getElementById('imgPreview');
	if (el.hasChildNodes()) {
		el.removeChild(el.childNodes[0]);
	}
	el.appendChild(imgObj);
	return imgObj;
}
function KindDrawImageEnd()
{
	var type = IMAGE_DOCUMENT.getElementById('imageType').value;
	var url = IMAGE_DOCUMENT.getElementById('imgLink').value;
	var file = IMAGE_DOCUMENT.getElementById('imgFile').value;
	var width = IMAGE_DOCUMENT.getElementById('imgWidth').value;
	var height = IMAGE_DOCUMENT.getElementById('imgHeight').value;
	var border = IMAGE_DOCUMENT.getElementById('imgBorder').value;
	var title = IMAGE_DOCUMENT.getElementById('imgTitle').value;
	var align = IMAGE_DOCUMENT.getElementById('imgAlign').value;
	var hspace = IMAGE_DOCUMENT.getElementById('imgHspace').value;
	var vspace = IMAGE_DOCUMENT.getElementById('imgVspace').value;
	if (type == 1) {
		if (file == '') {
			alert(MSG_SELECT_IMAGE);
			return false;
		}
		if (KindCheckImageFileType(file, "\\") == false) {
			return false;
		}
	} else {
		if (KindCheckImageFileType(url, "/") == false) {
			return false;
		}
	}
	if (width.match(/^\d+$/) == null) {
		alert(MSG_INVALID_WIDTH);
		return false;
	}
	if (height.match(/^\d+$/) == null) {
		alert(MSG_INVALID_HEIGHT);
		return false;
	}
	if (border.match(/^\d+$/) == null) {
		alert(MSG_INVALID_BORDER);
		return false;
	}
	if (hspace.match(/^\d+$/) == null) {
		alert(MSG_INVALID_HSPACE);
		return false;
	}
	if (vspace.match(/^\d+$/) == null) {
		alert(MSG_INVALID_VSPACE);
		return false;
	}
	var fileName;
	KindEditorForm.focus();
	if (type == 1) {
		fileName = KindGetFileName(file, "\\");
		var fileExt = KindGetFileExt(fileName);
		var dateObj = new Date();
		var year = dateObj.getFullYear().toString(10);
		var month = (dateObj.getMonth() + 1).toString(10);
		month = month.length < 2 ? '0' + month : month;
		var day = dateObj.getDate().toString(10);
		day = day.length < 2 ? '0' + day : day;
		var ymd = year + month + day;
		fileName = ymd + dateObj.getTime().toString(10) + '.' + fileExt;
		IMAGE_DOCUMENT.getElementById('fileName').value = fileName;
	} else {
		KindInsertImage(url, width, height, border, title, align, hspace, vspace);
	}
}
function KindInsertImage(url, width, height, border, title, align, hspace, vspace)
{
	var element = document.createElement("img");
	element.src = url;
	if (width > 0) {
		element.style.width = width;
	}
	if (height > 0) {
		element.style.height = height;
	}
	if (align != "") {
		element.align = align;
	}
	if (hspace > 0) {
		element.hspace = hspace;
	}
	if (vspace > 0) {
		element.vspace = vspace;
	}
	element.border = border;
	element.alt = KindHtmlentities(title);
	KindSelect();
	KindInsertItem(element);
	KindDisableMenu();
	KindReloadIframe();
}
function KindGetFlashHtmlTag(url, width, height)
{
	var str = '<object type="application/x-shockwave-flash" data="'+url+'" ' + 
			'width="'+width+'" height="'+height+'" wmode="transparent">' + 
			'<param name="movie" value="'+url+'" />' + 
			'<param name="wmode" value="transparent"/>' + 
			'</object>';
	return str;
}
function KindFlashPreview()
{
	var url = FLASH_DOCUMENT.getElementById('flashLink').value;
	if (KindCheckFlashFileType(url, "/") == false) {
		return false;
	}
	var el = FLASH_DOCUMENT.getElementById('flashPreview');
	el.innerHTML = KindGetFlashHtmlTag(url, '230', '230');
}
function KindDrawFlashEnd()
{
	var url = FLASH_DOCUMENT.getElementById('flashLink').value;
	if (KindCheckFlashFileType(url, "/") == false) {
		return false;
	}
	KindEditorForm.focus();
	KindSelect();
	var imgObj = document.createElement("IMG");
	imgObj.id = 'flashpreviewimg';
	imgObj.src = IMAGE_PATH + 'flashpreview.gif';
	imgObj.style.width = '100';
	imgObj.style.height = '100';
	imgObj.border = '0';
	imgObj.alt = url;
	KindInsertItem(imgObj);
	KindDisableMenu();
}
function KindGetMediaHtmlTag(cmd, url, width, height, title)
{
	var temp = title.split('-');
	var autostart = temp[0];
	var controls = temp[1];
	var str;
	if (cmd == "KIND_REAL") {
		str = '<object type="audio/x-pn-realaudio-plugin" data="'+url+'" ' + 
			'width="'+width+'" height="'+height+'">' + 
			'<param name="SRC" value="'+url+'" />' + 
			'<param name="AUTOSTART" value="'+autostart+'" />' + 
			'<param name="CONTROLS" value="'+controls+'" />' + 
			'</object>';
	} else {
		str = '<object type="video/x-ms-asf-plugin" data="'+url+'" ' + 
			'width="'+width+'" height="'+height+'">' + 
			'<param name="FileName" value="'+url+'" />' + 
			'<param name="AutoStart" value="'+autostart+'" />' + 
			'<param name="EnableContextMenu" value="'+controls+'" />' + 
			'</object>';
	}
	return str;
}
function KindMediaPreview(cmd)
{
	var mediaDocument;
	if (cmd == 'KIND_REAL') {
		mediaDocument = REAL_DOCUMENT;
	} else {
		mediaDocument = MEDIA_DOCUMENT;
	}
	var url = mediaDocument.getElementById(cmd+'link').value;
	if (KindCheckMediaFileType(cmd, url, "/") == false) {
		return false;
	}
	var el = mediaDocument.getElementById(cmd+'preview');
	var title;
	if (cmd == "KIND_REAL") {
		title = '1-ControlPanel,StatusBar';
	} else {
		title = '1-0';
	}
	el.innerHTML = KindGetMediaHtmlTag(cmd, url, 230, 230, title);
}
function KindDrawMediaEnd(cmd)
{
	var mediaDocument;
	if (cmd == 'KIND_REAL') {
		mediaDocument = REAL_DOCUMENT;
	} else {
		mediaDocument = MEDIA_DOCUMENT;
	}
	var url = mediaDocument.getElementById(cmd+'link').value;
	if (KindCheckMediaFileType(cmd, url, "/") == false) {
		return false;
	}
	KindEditorForm.focus();
	KindSelect();
	var imgObj = document.createElement("IMG");
	if (cmd == 'KIND_REAL') {
		imgObj.id = 'realpreviewimg';
		imgObj.src = IMAGE_PATH + 'realpreview.gif';
	} else {
		imgObj.id = 'mediapreviewimg';
		imgObj.src = IMAGE_PATH + 'mediapreview.gif';
	}
	imgObj.style.width = '100px';
	imgObj.style.height = '100px';
	if (cmd == "KIND_REAL") {
		imgObj.title = '1-ControlPanel,StatusBar';
	} else {
		imgObj.title = '1-0';
	}
	imgObj.border = '0';
	imgObj.alt = url;
	KindInsertItem(imgObj);
	KindDisableMenu(cmd);
}
function KindDrawLinkEnd()
{
	var range;
	var url = LINK_DOCUMENT.getElementById('hyperLink').value;
	var target = LINK_DOCUMENT.getElementById('hyperLinkTarget').value;
	if (url.match(/http:\/\/.{3,}/) == null) {
		alert(MSG_INPUT_URL);
		return false;
	}
	KindEditorForm.focus();
	KindSelect();
	var element;
    if (BROWSER == 'IE') {
		if (SELECTION.type.toLowerCase() == 'control') {
			var el = document.createElement("a");
			el.href = url;
			if (target) {
				el.target = target;
			}
			RANGE.item(0).applyElement(el);
		} else if (SELECTION.type.toLowerCase() == 'text') {
			KindExecuteValue('CreateLink', url);
			element = RANGE.parentElement();
			if (target) {
				element.target = target;
			}
		}
	} else {
		KindExecuteValue('CreateLink', url);
		element = RANGE.startContainer.previousSibling;
		element.target = target;
		if (target) {
			element.target = target;
		}
    }
	KindDisableMenu();
}
function KindSelection()
{
	if (BROWSER == 'IE') {
		SELECTION = EDITFORM_DOCUMENT.selection;
		RANGE = SELECTION.createRange();
		RANGE_TEXT = RANGE.text;
	} else {
		SELECTION = document.getElementById("KindEditorForm").contentWindow.getSelection();
        RANGE = SELECTION.getRangeAt(0);
		RANGE_TEXT = RANGE.toString();
	}
}
function KindSelect()
{
	if (BROWSER == 'IE') {
		RANGE.select();
	}
}
function KindInsertItem(insertNode)
{
	if (BROWSER == 'IE') {
		if (SELECTION.type.toLowerCase() == 'control') {
			RANGE.item(0).outerHTML = insertNode.outerHTML;
		} else {
			RANGE.pasteHTML(insertNode.outerHTML);
		}
	} else {
        SELECTION.removeAllRanges();
		RANGE.deleteContents();
        var startRangeNode = RANGE.startContainer;
        var startRangeOffset = RANGE.startOffset;
        var newRange = document.createRange();
		if (startRangeNode.nodeType == 3 && insertNode.nodeType == 3) {
            startRangeNode.insertData(startRangeOffset, insertNode.nodeValue);
            newRange.setEnd(startRangeNode, startRangeOffset + insertNode.length);
            newRange.setStart(startRangeNode, startRangeOffset + insertNode.length);
        } else {
            var afterNode;
            if (startRangeNode.nodeType == 3) {
                var textNode = startRangeNode;
                startRangeNode = textNode.parentNode;
                var text = textNode.nodeValue;
                var textBefore = text.substr(0, startRangeOffset);
                var textAfter = text.substr(startRangeOffset);
                var beforeNode = document.createTextNode(textBefore);
                var afterNode = document.createTextNode(textAfter);
                startRangeNode.insertBefore(afterNode, textNode);
                startRangeNode.insertBefore(insertNode, afterNode);
                startRangeNode.insertBefore(beforeNode, insertNode);
                startRangeNode.removeChild(textNode);
            } else {
				if (startRangeNode.tagName.toLowerCase() == 'html') {
					startRangeNode = startRangeNode.childNodes[0].nextSibling;
					afterNode = startRangeNode.childNodes[0];
				} else {
					afterNode = startRangeNode.childNodes[startRangeOffset];
				}
				startRangeNode.insertBefore(insertNode, afterNode);
            }
            newRange.setEnd(afterNode, 0);
            newRange.setStart(afterNode, 0);
        }
        SELECTION.addRange(newRange);
	}
}
function KindExecuteValue(cmd, value)
{
	EDITFORM_DOCUMENT.execCommand(cmd, false, value);
}
function KindSimpleExecute(cmd)
{
	KindEditorForm.focus();
	EDITFORM_DOCUMENT.execCommand(cmd, false, null);
	KindDisableMenu();
}
function KindExecute(cmd, value)
{
	switch (cmd)
	{
		case 'KIND_SOURCE':
			var length = document.getElementById(TOP_TOOLBAR_ICON[0][0]).src.length - 10;
			var image = document.getElementById(TOP_TOOLBAR_ICON[0][0]).src.substr(length,10);
			if (image == 'source.gif') {
				document.getElementById("KindCodeForm").value = KindImageToObj(KindHtmlToXhtml(EDITFORM_DOCUMENT.body.innerHTML));
				document.getElementById("KindEditorIframe").style.display = 'none';
				document.getElementById("KindEditTextarea").style.display = 'block';
				KindDisableToolbar(true);
			} else {
				EDITFORM_DOCUMENT.body.innerHTML = KindClearScriptTag(KindObjToImage(document.getElementById("KindCodeForm").value));
				document.getElementById("KindEditTextarea").style.display = 'none';
				document.getElementById("KindEditorIframe").style.display = 'block';
				KindDisableToolbar(false);
			}
			KindDisableMenu();
			break;
		case 'KIND_PRINT':
			KindSimpleExecute('print');
			break;
		case 'KIND_UNDO':
			KindSimpleExecute('undo');
			break;
		case 'KIND_REDO':
			KindSimpleExecute('redo');
			break;
		case 'KIND_CUT':
			KindSimpleExecute('cut');
			break;
		case 'KIND_COPY':
			KindSimpleExecute('copy');
			break;
		case 'KIND_PASTE':
			KindSimpleExecute('paste');
			break;
		case 'KIND_SELECTALL':
			KindSimpleExecute('selectall');
			break;
		case 'KIND_SUBSCRIPT':
			KindSimpleExecute('subscript');
			break;
		case 'KIND_SUPERSCRIPT':
			KindSimpleExecute('superscript');
			break;
		case 'KIND_BOLD':
			KindSimpleExecute('bold');
			break;
		case 'KIND_ITALIC':
			KindSimpleExecute('italic');
			break;
		case 'KIND_UNDERLINE':
			KindSimpleExecute('underline');
			break;
		case 'KIND_STRIKE':
			KindSimpleExecute('strikethrough');
			break;
		case 'KIND_JUSTIFYLEFT':
			KindSimpleExecute('justifyleft');
			break;
		case 'KIND_JUSTIFYCENTER':
			KindSimpleExecute('justifycenter');
			break;
		case 'KIND_JUSTIFYRIGHT':
			KindSimpleExecute('justifyright');
			break;
		case 'KIND_JUSTIFYFULL':
			KindSimpleExecute('justifyfull');
			break;
		case 'KIND_NUMBEREDLIST':
			KindSimpleExecute('insertorderedlist');
			break;
		case 'KIND_UNORDERLIST':
			KindSimpleExecute('insertunorderedlist');
			break;
		case 'KIND_INDENT':
			KindSimpleExecute('indent');
			break;
		case 'KIND_OUTDENT':
			KindSimpleExecute('outdent');
			break;
		case 'KIND_REMOVE':
			KindSimpleExecute('removeformat');
			break;
		case 'KIND_ZOOM':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_ZOOM_END':
			KindEditorForm.focus();
			EDITFORM_DOCUMENT.body.style.zoom = value;
			KindDisableMenu();
			break;
		case 'KIND_TITLE':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_TITLE_END':
			KindEditorForm.focus();
			value = '<' + value + '>';
			KindSelect();
			KindExecuteValue('FormatBlock', value);
			KindDisableMenu();
			break;
		case 'KIND_FONTNAME':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_FONTNAME_END':
			KindEditorForm.focus();
			KindSelect();
			KindExecuteValue('fontname', value);
			KindDisableMenu();
			break;
		case 'KIND_FONTSIZE':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_FONTSIZE_END':
			KindEditorForm.focus();
			value = value.substr(0, 1);
			KindSelect();
			KindExecuteValue('fontsize', value);
			KindDisableMenu();
			break;
		case 'KIND_TEXTCOLOR':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_TEXTCOLOR_END':
			KindEditorForm.focus();
			KindSelect();
			KindExecuteValue('ForeColor', value);
			KindDisableMenu();
			break;
		case 'KIND_BGCOLOR':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_BGCOLOR_END':
			KindEditorForm.focus();
			if (BROWSER == 'IE') {
				KindSelect();
				KindExecuteValue('BackColor', value);
			} else {
				var startRangeNode = RANGE.startContainer;
				if (startRangeNode.nodeType == 3) {
					var parent = startRangeNode.parentNode;
					var element = document.createElement("font");
					element.style.backgroundColor = value;
					element.appendChild(RANGE.extractContents());
					var startRangeOffset = RANGE.startOffset;
					var newRange = document.createRange();
					var afterNode;
					var textNode = startRangeNode;
					startRangeNode = textNode.parentNode;
					var text = textNode.nodeValue;
					var textBefore = text.substr(0, startRangeOffset);
					var textAfter = text.substr(startRangeOffset);
					var beforeNode = document.createTextNode(textBefore);
					var afterNode = document.createTextNode(textAfter);
					startRangeNode.insertBefore(afterNode, textNode);
					startRangeNode.insertBefore(element, afterNode);
					startRangeNode.insertBefore(beforeNode, element);
					startRangeNode.removeChild(textNode);
					newRange.setEnd(afterNode, 0);
					newRange.setStart(afterNode, 0);
					SELECTION.addRange(newRange);
				}
			}
			KindDisableMenu();
			break;
		case 'KIND_ICON':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_ICON_END':
			KindEditorForm.focus();
			var element = document.createElement("img");
			element.src = value;
			element.border = 0;
			element.alt = "";
			KindSelect();
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_IMAGE':
			KindDisplayMenu(cmd);
			KindImageIframe.focus();
			IMAGE_DOCUMENT.getElementById(cmd+'submitButton').focus();
			break;
		case 'KIND_FLASH':
			KindDisplayMenu(cmd);
			KindFlashIframe.focus();
			FLASH_DOCUMENT.getElementById(cmd+'submitButton').focus();
			break;
		case 'KIND_MEDIA':
			KindDisplayMenu(cmd);
			KindMediaIframe.focus();
			MEDIA_DOCUMENT.getElementById(cmd+'submitButton').focus();
			break;
		case 'KIND_REAL':
			KindDisplayMenu(cmd);
			KindRealIframe.focus();
			REAL_DOCUMENT.getElementById(cmd+'submitButton').focus();
			break;
		case 'KIND_LINK':
			KindDisplayMenu(cmd);
			KindLinkIframe.focus();
			LINK_DOCUMENT.getElementById(cmd+'submitButton').focus();
			break;
		case 'KIND_UNLINK':
			KindSimpleExecute('unlink');
			break;
		case 'KIND_SPECIALCHAR':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_SPECIALCHAR_END':
			KindEditorForm.focus();
			KindSelect();
			var element = document.createElement("span");
			element.appendChild(document.createTextNode(value));
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_LAYER':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_LAYER_END':
			KindEditorForm.focus();
			var element = document.createElement("div");
			element.style.padding = "5px";
			element.style.border = "1px solid #AAAAAA";
			element.style.backgroundColor = value;
			var childElement = document.createElement("div");
			childElement.innerHTML = STR_INPUT_CONTENT;
			element.appendChild(childElement);
			KindSelect();
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_TABLE':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_TABLE_END':
			KindEditorForm.focus();
			var location = value.split(',');
			var element = document.createElement("table");
			element.cellPadding = 0;
			element.cellSpacing = 0;
			element.border = 1;
			element.style.width = "100px";
			element.style.height = "100px";
			for (var i = 0; i < location[0]; i++) {
				var rowElement = element.insertRow(i);
				for (var j = 0; j < location[1]; j++) {
					var cellElement = rowElement.insertCell(j);
					cellElement.innerHTML = "&nbsp;";
				}
			}
			KindSelect();
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_HR':
			KindDisplayMenu(cmd);
			break;
		case 'KIND_HR_END':
			KindEditorForm.focus();
			var element = document.createElement("hr");
			element.width = "100%";
			element.color = value;
			element.size = 1;
			KindSelect();
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_DATE':
			KindEditorForm.focus();
			KindSelection();
			var date = new Date();
			var year = date.getFullYear().toString(10);
			var month = (date.getMonth() + 1).toString(10);
			month = month.length < 2 ? '0' + month : month;
			var day = date.getDate().toString(10);
			day = day.length < 2 ? '0' + day : day;
			var value = year + '-' + month + '-' + day;
			var element = document.createElement("span");
			element.appendChild(document.createTextNode(value));
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_TIME':
			KindEditorForm.focus();
			KindSelection();
			var date = new Date();
			var hour = date.getHours().toString(10);
			hour = hour.length < 2 ? '0' + hour : hour;
			var minute = date.getMinutes().toString(10);
			minute = minute.length < 2 ? '0' + minute : minute;
			var second = date.getSeconds().toString(10);
			second = second.length < 2 ? '0' + second : second;
			var value = hour + ':' + minute + ':' + second;
			var element = document.createElement("span");
			element.appendChild(document.createTextNode(value));
			KindInsertItem(element);
			KindDisableMenu();
			break;
		case 'KIND_PREVIEW':
			eval(OBJ_NAME).data();
			var newWin = window.open('', 'kindPreview','width=800,height=600,left=30,top=30,resizable=yes,scrollbars=yes');
			KindWriteFullHtml(newWin.document, document.getElementsByName(eval(OBJ_NAME).hiddenName)[0].value);
			KindDisableMenu();
			break;
		case 'KIND_ABOUT':
			KindDisplayMenu(cmd);
			break;
		default: 
			break;
	}
}
function KindDisableToolbar(flag)
{
	if (flag == true) {
		document.getElementById(TOP_TOOLBAR_ICON[0][0]).src = IMAGE_PATH + 'design.gif';
		for (i = 0; i < TOOLBAR_ICON.length; i++) {
			var el = document.getElementById(TOOLBAR_ICON[i][0]);
			if (TOOLBAR_ICON[i][0] == 'KIND_SOURCE' || TOOLBAR_ICON[i][0] == 'KIND_PREVIEW' || TOOLBAR_ICON[i][0] == 'KIND_ABOUT') {
				continue;
			}
			el.style.visibility = 'hidden';
		}
	} else {
		document.getElementById(TOP_TOOLBAR_ICON[0][0]).src = IMAGE_PATH + 'source.gif';
		for (i = 0; i < TOOLBAR_ICON.length; i++) {
			var el = document.getElementById(TOOLBAR_ICON[i][0]);
			el.style.visibility = 'visible';
			EDITFORM_DOCUMENT.designMode = 'On';
		}
	}
}
function KindCreateIcon(icon)
{
	var str = '<img id="'+ icon[0] +'" src="' + IMAGE_PATH + icon[1] + '" alt="' + icon[2] + '" title="' + icon[2] + 
			'" align="absmiddle" style="border:1px solid ' + TOOLBAR_BG_COLOR +';cursor:pointer;height:20px;';
	str += '" onclick="javascript:KindExecute(\''+ icon[0] +'\');" '+
			'onmouseover="javascript:this.style.border=\'1px solid ' + MENU_BORDER_COLOR + '\';" ' +
			'onmouseout="javascript:this.style.border=\'1px solid ' + TOOLBAR_BG_COLOR + '\';" ';
	str += '>';
	return str;
}
function KindCreateToolbar()
{
	var htmlData = '<table cellpadding="0" cellspacing="0" border="0" height="26"><tr>';
	if (EDITOR_TYPE == 'full') {
		for (i = 0; i < TOP_TOOLBAR_ICON.length; i++) {
			htmlData += '<td style="padding:2px;">' + KindCreateIcon(TOP_TOOLBAR_ICON[i]) + '</td>';
		}
		htmlData += '</tr></table><table cellpadding="0" cellspacing="0" border="0" height="26"><tr>';
		for (i = 0; i < BOTTOM_TOOLBAR_ICON.length; i++) {
			htmlData += '<td style="padding:2px;">' + KindCreateIcon(BOTTOM_TOOLBAR_ICON[i]) + '</td>';
		}
	} else {
		for (i = 0; i < SIMPLE_TOOLBAR_ICON.length; i++) {
			htmlData += '<td style="padding:2px;">' + KindCreateIcon(SIMPLE_TOOLBAR_ICON[i]) + '</td>';
		}
	}
	htmlData += '</tr></table>';
	return htmlData;
}
function KindWriteFullHtml(documentObj, content)
{
	var editHtmlData = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">\r\n';
	editHtmlData += '<html xmlns="http://www.w3.org/1999/xhtml">\r\n<head>\r\n<title>KindEditor</title>\r\n<style type="text/css">\r\np {margin:0;}\r\n</style>\r\n</head>\r\n';
	editHtmlData += '<body style="font-size:12px;font-family:'+EDITOR_FONT_FAMILY+';margin:2px;background-color:' + FORM_BG_COLOR + '">\r\n';
	editHtmlData += content;
	editHtmlData += '\r\n</body>\r\n</html>\r\n';
	documentObj.open();
	documentObj.write(editHtmlData);
	documentObj.close();
}
function KindEditor(objName) 
{
	this.objName = objName;
	this.hiddenName = objName;
	this.siteDomain = "";
	this.editorType = "full"; //full or simple
	this.safeMode = false; // true or false
	this.uploadMode = true; // true or false
	this.editorWidth = "700px";
	this.editorHeight = "400px";
	this.skinPath = './skins/default/';
	this.iconPath = './icons/';
	this.imageAttachPath = './attached/';
	this.imageUploadCgi = "./upload_cgi/upload.php";
	this.menuBorderColor = '#AAAAAA';
	this.menuBgColor = '#EFEFEF';
	this.menuTextColor = '#222222';
	this.menuSelectedColor = '#CCCCCC';
	this.toolbarBorderColor = '#DDDDDD';
	this.toolbarBgColor = '#EFEFEF';
	this.formBorderColor = '#DDDDDD';
	this.formBgColor = '#FFFFFF';
	this.buttonColor = '#AAAAAA';
	this.init = function()
	{
		SITE_DOMAIN = this.siteDomain;
		EDITOR_TYPE = this.editorType.toLowerCase();
		SAFE_MODE = this.safeMode;
		UPLOAD_MODE = this.uploadMode;
		IMAGE_PATH = this.skinPath;
		ICON_PATH = this.iconPath;
		IMAGE_ATTACH_PATH = this.imageAttachPath;
		IMAGE_UPLOAD_CGI = this.imageUploadCgi;
		MENU_BORDER_COLOR = this.menuBorderColor;
		MENU_BG_COLOR = this.menuBgColor;
		MENU_TEXT_COLOR = this.menuTextColor;
		MENU_SELECTED_COLOR = this.menuSelectedColor;
		TOOLBAR_BORDER_COLOR = this.toolbarBorderColor;
		TOOLBAR_BG_COLOR = this.toolbarBgColor;
		FORM_BORDER_COLOR = this.formBorderColor;
		FORM_BG_COLOR = this.formBgColor;
		BUTTON_COLOR = this.buttonColor;
		OBJ_NAME = this.objName;
		BROWSER = KindGetBrowser();
		TOOLBAR_ICON = Array();
		for (var i = 0; i < TOP_TOOLBAR_ICON.length; i++) {
			TOOLBAR_ICON.push(TOP_TOOLBAR_ICON[i]);
		}
		for (var i = 0; i < BOTTOM_TOOLBAR_ICON.length; i++) {
			TOOLBAR_ICON.push(BOTTOM_TOOLBAR_ICON[i]);
		}
	}
	this.show = function()
	{
		this.init();
		var widthStyle = 'width:' + this.editorWidth + ';';
		var widthArr = this.editorWidth.match(/(\d+)([px%]{1,2})/);
		var iframeWidthStyle = 'width:' + (parseInt(widthArr[1]) - 2).toString(10) + widthArr[2] + ';';
		var heightStyle = 'height:' + this.editorHeight + ';';
		var heightArr = this.editorHeight.match(/(\d+)([px%]{1,2})/);
		var iframeHeightStyle = 'height:' + (parseInt(heightArr[1]) - 3).toString(10) + heightArr[2] + ';';
		if (BROWSER == '') {
			var htmlData = '<div id="KindEditTextarea" style="' + widthStyle + heightStyle + '">' +
			'<textarea name="KindCodeForm" id="KindCodeForm" style="' + widthStyle + heightStyle + 
			'padding:0;margin:0;border:1px solid '+ FORM_BORDER_COLOR + 
			';font-size:12px;line-height:16px;font-family:'+EDITOR_FONT_FAMILY+';background-color:'+ 
			FORM_BG_COLOR +';">' + document.getElementsByName(this.hiddenName)[0].value + '</textarea></div>';
			document.open();
			document.write(htmlData);
			document.close();
			return;
		}
		var htmlData = '<div style="font-family:'+EDITOR_FONT_FAMILY+';">';
		htmlData += '<div style="'+widthStyle+';border:1px solid ' + TOOLBAR_BORDER_COLOR + ';background-color:'+ TOOLBAR_BG_COLOR +'">';
		htmlData += KindCreateToolbar();
		htmlData += '</div><div id="KindEditorIframe" style="' + widthStyle + heightStyle + 
			'border:1px solid '+ FORM_BORDER_COLOR +';border-top:0;">' +
			'<iframe name="KindEditorForm" id="KindEditorForm" frameborder="0" style="' + iframeWidthStyle + iframeHeightStyle + 
			'padding:0;margin:0;border:0;"></iframe></div>';
		if (EDITOR_TYPE == 'full') {
			htmlData += '<div id="KindEditTextarea" style="' + widthStyle + heightStyle + 
				'border:1px solid '+ FORM_BORDER_COLOR +';background-color:'+ 
				FORM_BG_COLOR +';border-top:0;display:none;">' +
				'<textarea name="KindCodeForm" id="KindCodeForm" style="' + iframeWidthStyle + iframeHeightStyle + 
				'padding:0;margin:0;border:0;font-size:12px;line-height:16px;font-family:'+EDITOR_FONT_FAMILY+';background-color:'+ 
				FORM_BG_COLOR +';" onclick="javascirit:KindDisableMenu();"></textarea></div>';
		}
		htmlData += '</div>';
		for (var i = 0; i < POPUP_MENU_TABLE.length; i++) {
			if (POPUP_MENU_TABLE[i] == 'KIND_IMAGE') {
				htmlData += '<span id="InsertIframe">';
			}
			htmlData += KindPopupMenu(POPUP_MENU_TABLE[i]);
			if (POPUP_MENU_TABLE[i] == 'KIND_REAL') {
				htmlData += '</span>';
			}
		}
		document.open();
		document.write(htmlData);
		document.close();
		if (BROWSER == 'IE') {
			EDITFORM_DOCUMENT = document.frames("KindEditorForm").document;
		} else {
			EDITFORM_DOCUMENT = document.getElementById('KindEditorForm').contentDocument;
		}
		KindDrawIframe('KIND_IMAGE');
		KindDrawIframe('KIND_FLASH');
		KindDrawIframe('KIND_MEDIA');
		KindDrawIframe('KIND_REAL');
		KindDrawIframe('KIND_LINK');
		EDITFORM_DOCUMENT.designMode = 'On';
		KindWriteFullHtml(EDITFORM_DOCUMENT, document.getElementsByName(eval(OBJ_NAME).hiddenName)[0].value);
		var el = EDITFORM_DOCUMENT.body;
		if (el.addEventListener){
			el.addEventListener('click', KindDisableMenu, false); 
		} else if (el.attachEvent){
			el.attachEvent('onclick', KindDisableMenu);
		}
	}
	this.data = function()
	{
		var htmlResult;
		if (BROWSER == '') {
			htmlResult = document.getElementById("KindCodeForm").value;
		} else {
			if (EDITOR_TYPE == 'full') {
				var length = document.getElementById(TOP_TOOLBAR_ICON[0][0]).src.length - 10;
				var image = document.getElementById(TOP_TOOLBAR_ICON[0][0]).src.substr(length,10);
				if (image == 'source.gif') {
					htmlResult = EDITFORM_DOCUMENT.body.innerHTML;
				} else {
					htmlResult = document.getElementById("KindCodeForm").value;
				}
			} else {
				htmlResult = EDITFORM_DOCUMENT.body.innerHTML;
			}
		}
		KindDisableMenu();
		htmlResult = KindHtmlToXhtml(htmlResult);
		htmlResult = KindImageToObj(htmlResult);
		htmlResult = KindClearScriptTag(htmlResult);
		document.getElementsByName(this.hiddenName)[0].value = htmlResult;
		return htmlResult;
	}
}
