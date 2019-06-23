const SPENDING_THRESHOLD = 200;
const TAX_RATE = 0.08;
const PHONE_PRICE = 99.99;
const ACCESSORY_PRICE = 9.99;
var bank_balance = 303.91;
var amount = 0;
var count = 0;
var ac=0;
function totalcost(price){
    return price*(1+TAX_RATE);
}
function findnumber(){
    while(totalcost(amount+PHONE_PRICE)<bank_balance){
        amount+=PHONE_PRICE;
        count++;
        if(totalcost(amount+ACCESSORY_PRICE)<bank_balance){
            amount+=ACCESSORY_PRICE;
            ac++;
        }
    }
}
function report(){
    alert("This script calculate the total amount purchased and the count")
    console.log("The total cost will be  $"+ totalcost(amount).toFixed(2)+" with "+ count + " phone and "+ ac+ " accesorries");
}
findnumber();
report();