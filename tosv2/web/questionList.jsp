<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.must.tos.bean.Question"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.must.tos.util.FunctionUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>常見問題與答案</title>
<script type="text/javascript" src="js/jquery-1.2.6.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript">
$(document).ready(function(){
	$('#editTable').hide();
	$('input[@name=cancelbtn]').click(function(){
		$('#question').attr("value", '');
		$('#answer').attr("value", '');
		$('#status').attr("value", "Y");
		$('#editTable').hide();
	})
	
	$('#addbtn').click(function(){
		$('#question').attr("value", '');
		$('#answer').attr("value", '');
		$('#status').attr("value", "Y");
		$('#editTable').show();
	})
	
	$('input[@name=savebtn]').click(function(){
		var question = $('#question').val();
		var answer = $('#answer').val();
		var editId = $('input[@name=editId]').val();
		var status = $('#status').val();
		if(question == ""){
			alert("問題內容不能為空！");
			return false;
		}
		if(answer == ""){
			alert("答案內容不能為空！");
			return false;
		}
		
		$.post(
			"QuestionServlet",
			{
				type: "editQuestion",
				editId: editId,
				question: question,
				answer: answer,
				actind: status
			},
			function(result){
				if(result == 0){
					alert("問題與答案添加成功！");
					window.location.href = "QuestionServlet";
				}else{
					alert("問題與答案添加失敗！");
				}
			}
		)
	})
})
function editPage(id){
	$.post(
		"QuestionServlet",
		{
			type: "getQuestion",
			id: id
		},
		function(result){
			var data = result.split("&");
			
			var question = data[0];
			var answer = data[1];
			var actind = data[2];
			
			$('#editTable').show();
			$('#question').attr("value", question);
			$('#answer').attr("value", answer);
			$('input[@name=editId]').attr("value", id);
			$('#status').attr("value", actind);
		}
	)
}
function countlen(){
	//if(document.getElementById("answer").value.length > 160){
	//	alert("字數已經超過160！！");
	//	document.getElementById("answer").value = document.getElementById("answer").value.substring(0, 160);
	//}
	return true;
}
</script>
<script type="text/javascript" src="js/timeout.js"></script>
<script language="javascript">
var timeout = new Timeout("timeout", <%= session.getMaxInactiveInterval()%>);
timeout.start();
</script>
</head>
<body>
<%
List<Question> questionList = null;
if(request.getSession().getAttribute("questionList") != null){
	questionList = (List)request.getSession().getAttribute("questionList");
}
%>
<h2>常見問題與答案</h2>
<table width="98%" border="0" cellpadding="1" cellspacing="1">
  <tr bgcolor="#C6D6FD">
    <th width="5%" height="25">序號</th>
    <th width="35%">問題</th>
    <th width="50%">答案</th>
    <th width="5%">狀態</th>
    <th width="5%">操作</th>
  </tr>
  <%
  int i = 0;
  if(questionList != null && !questionList.isEmpty()){
	  for(Question question : questionList){
		  i++;
  %>
  <tr onMouseOver="this.bgColor='#FFFFFF'" onMouseOut="this.bgColor='#FFFBEF'" bgcolor="#FFFBEF">
    <td height="20" align="center"><%=i %></td>
    <td>
    <%
    String questionStr = FunctionUtil.Html2Text(question.getQuestion());
    if(questionStr != null && questionStr.length()>0){
    	if(questionStr.length() > 36){
    		out.print(questionStr.substring(0, 36)+"...");
    	}else{
    		out.print(questionStr);
    	}
    }
    %>
    </td>
    <td align="center">
    <%
    String answerStr = FunctionUtil.Html2Text(question.getAnswer());
    if(answerStr != null && answerStr.length()>0){
    	if(answerStr.length() > 36){
    		out.print(answerStr.substring(0, 36)+"...");
    	}else{
    		out.print(answerStr);
    	}
    }
    %>
    </td>
    <td align="center"><%="Y".equals(question.getActInd())?"顯示":"不顯示" %></td>
    <td align="center">
      <a href="#" onClick="editPage('<%=question.getId() %>');">編輯</a>
    </td>
  </tr>
  <%
  	  }
  }else {
  %>
  <tr>
    <td colspan="5" height="25">沒有任何資料可顯示！</td>
  </tr>
  <%
  }
  %>
</table>
<p><input type="button" id="addbtn" value="新 增" ></p>
<!-- ==================Edit Page================= -->
<input type="hidden" name="editId" value="0">
<table width="97%" border="0" cellpadding="1" cellspacing="1" id="editTable">
  <tr>
    <th colspan="2">常見問題與答案</th>
  </tr>
  <tr>
    <td width="10%">問題：</td>
    <td width="90%">
      <input type="text" name="question" id="question" style="width: 80%">
    </td>
  </tr>
  <tr>
    <td>答案：</td>
    <td>
      <textarea rows="6" cols="100" name="answer" id="answer" onKeyUp="countlen()"></textarea>
    </td>
  </tr>
  <tr>
    <td>狀態:</td>
    <td>
      <select name="status" id="status">
        <option value="Y">顯示</option>
        <option value="N">不顯示</option>
      </select>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      <input type="button" name="savebtn" value="保 存">
      &nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" name="cancelbtn" value="取 消">
    </td>
  </tr>
</table>
<!-- ==================Edit Page================= -->
</body>
</html>
