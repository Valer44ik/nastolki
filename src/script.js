//функции для кнопок в "campaignList.html"
function buttonEnded(doc){
    window.open('./viewCampaign.html', '_self');
}
function buttonNotEnded(doc){
    window.open('./continueCampaign.html', '_self');
}


//функция для кнопки в "startACampaign.html"
function saveButton(doc){
    window.open('./campaignList.html', '_self');
    window.alert('Campaign has been saved.');
}


function startAFirstMatchButton(doc){
    window.open('./campaignSheet.html')
}
/*
функция для создания опреденной кнопки в зависимости от того, 
закончилась кампания или нет. 
Функция для работы с "camapignList.html"
function continueCampaign(index)
{
    var tale = document.getElementById('copmanyList');
    var status = table.rows[index+1].cells[2].innerHTML;

    if(status ==='notEnded'){
        var buttonNotEnded = document.createElement('button');
        buttonNotEnded.textContent = 'Continue campaign';
        buttonNotEnded.addEventListener('click',function(){
            window.location.href = 'startANewMatch.html';
        })
    }
    else{
        var buttonEnded = document.createElement('button');
        buttonEnded.textContent = 'View campaign';
        buttonEnded.addEventListener('click', function(){
            window.location.href = 'viewCampaign.html';
        })
    }

}
*/

