//保存历史数据
function saveHistoryData(currentPageNumber, id,  callback) {
    $(function(){
        $.ajax({
            url:'/saveHistoryData.do',
            method:'post',
            data:{"data" : parseInt(currentPageNumber), "id": id},
            async: false,
            success:function(data){
                console.log('成功');
                callback();
            },
            error:function(){console.log('保存历史记录出错')}
        });
    });
}