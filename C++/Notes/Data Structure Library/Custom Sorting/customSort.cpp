#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;
void printarr(std::vector<int> arr){
    std::cout<<"{";
    for(int i=0;i<arr.size();i++){
        std::cout<<arr[i];
        if(i!=arr.size()-1){
            std::cout<<", ";
        }
    }
    std::cout<<"}"<<endl;
}
int main(){
    vector<int> arr = {2,17,-15,3,4,7,8,-24,68,12,78,-5,0};
    sort(arr.begin(),arr.end());
    printarr(arr);
    //sort by parity and in decending order
    sort(arr.begin(),arr.end(),
    [](int a, int b){
        if((a&1)==(b&1))return a>b;
        else if(a&1) return false;
        return true;
    });
    printarr(arr);
}
