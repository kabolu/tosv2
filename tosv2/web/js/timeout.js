function Timeout(name, sec , want) {
  this.msg = {
    start : '本系統將會在',
    end   : '秒鐘後關閉!!'
  };
  this.name = name;
  this.sec = sec;
  this.buf = 10;
  this.want = (want==undefined)?60:want;
  this.deadline;
  this.remain;
  this.reset();
}
Timeout.prototype.reset = function() {
  var msec = (this.sec - this.buf)*1000;
  var now = new Date();
  this.deadline = new Date(now.getTime() + msec);
}
Timeout.prototype.start = function() {
  if(this.remain) {
    var now = new Date();
    this.deadline = new Date(now.getTime() + this.remain);
  }
  this.runid = window.setInterval(this.name + ".run()", 1000);
}
Timeout.prototype.stop = function() {
  if(this.runid) {
    var now = new Date();
    this.remain = this.deadline - now;
    window.clearInterval(this.runid);
    this.runid=null;
  }
}
Timeout.prototype.run = function() {
  var now = new Date();
  window.status = "";
  if (this.deadline - now < this.want*1000){
	  window.status = this.msg.start + " " + Math.round((this.deadline - now) /1000)+ " " + this.msg.end;
  }

  if (this.deadline < now ) {
    this.stop();
    TestCall();
    RedirectLogin();
  }
}
function RedirectLogin(){
	window.top.location.href = 'logout.jsp';
}