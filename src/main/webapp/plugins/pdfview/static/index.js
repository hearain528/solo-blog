//页面加载   获取全部信息
$(function(){
    $.get("/console/pdfview/index",function(result){
        //result数据添加到box容器中;
        console.log(result);
        addBox(result.data);
    });
});

function addBox(result){
    //result是一个集合,所以需要先遍历
    $.each(result,function(index,obj){
      $("#box").append("<tr>" +
          "<td>" + obj.realname + "</td>" +
          "<td>" + (obj.size/1000/1000).toFixed(2) + "M</td>" +
          "<td>" + obj.currentPage + "</td>" +
          "<td>" + "<a href='viewer.html?fileid=" + obj.idname + "'><button type=\"button\" class=\"btn btn-info\">点击预览</button></a>" + "</td>" +
          "<td>" + "<a href='delete?fileid=" + obj.idname + "'><button type=\"button\" class=\"btn btn-danger\">删除文件</button></a>" + "</td>" +
          "</tr>"
      );
    });
}
