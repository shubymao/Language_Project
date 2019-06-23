function makeadder(x){
    function add(y){
        return Number(x)+Number(y);
    }
    function add(y,z){
        return Number(x)+Number(y)+Number(z);
    }
    return add;
}
const result_h2= document.getElementById("result");
var a = prompt("Enter the first Number");
var adder= makeadder(a);
a = prompt("Enter the second number Number");
var b = prompt("Enter the third number");
result_h2.innerHTML= `The result is ${adder(a,b)}`;
