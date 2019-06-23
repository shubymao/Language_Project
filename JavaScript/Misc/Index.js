const item1_h1 = document.getElementById("Item1");
function changecontent(){
    const temp = item1_h1.innerHTML;
    if(temp==="Yes, It Can")item1_h1.innerHTML="Can This Text be changed?";
    else item1_h1.innerHTML="Yes, It Can";
}

item1_h1.addEventListener("click",changecontent);
