#include <iostream>
#include <vector>
#include <algorithm>
#include<functional>
///////////////////////////////////////////////////////////////////////////
//  Upper bound give pointer that is strictly greater (>k) than the given value
//  The upper bound for number 6 of vector {0, 1, 2, 3, 4, 5, 6, 7, 8} is 7
///////////////////////////////////////////////////////////////////////////
//  Lower bound give pointer that is not bigger  (>= k)than than
//  The lower bound for number 6 of vector {0, 1, 2, 3, 4, 5, 6, 7, 8} is 6
///////////////////////////////////////////////////////////////////////////
//  Other Example
//  The upper bound for number 6 of vector{0, 1, 2, 3, 4, 5, 7, 8}is 7
//  The lower bound for number 6 of vector{0, 1, 2, 3, 4, 5, 7, 8}is 7
///////////////////////////////////////////////////////////////////////////
void printarr(std::vector<int> arr){
    std::cout<<"{";
    for(int i=0;i<arr.size();i++){
        std::cout<<arr[i];
        if(i!=arr.size()-1){
            std::cout<<", ";
        }
    }
    std::cout<<"}";
}

int main() {
    std::vector<int> arr={0,1,2,3,4,5,6,7,8};
    int x = 6;
    std::cout<< "The upper bound for number "<< x <<" of vector";
    printarr(arr);
    std:: cout << "is "<<*std::upper_bound(arr.begin(),arr.end(),x)<<std::endl;
    std::cout<< "The lower bound for number "<< x <<" of vector";
    printarr(arr);
    std:: cout << "is "<<*std::lower_bound(arr.begin(),arr.end(),x)<<std::endl;

    std::vector<int> arr2={0,1,2,3,4,5,7,8};
    std::cout<< "The upper bound for number "<< x <<" of vector";
    printarr(arr2);
    std:: cout << "is "<<*std::upper_bound(arr2.begin(),arr2.end(),x)<<std::endl;
    std::cout<< "The lower bound for number "<< x <<" of vector";
    printarr(arr2);
    std:: cout << "is "<<*std::lower_bound(arr2.begin(),arr2.end(),x)<<std::endl;
    std:: cout <<"lower bound it is the "<<distance(arr2.begin(),std::lower_bound(arr2.begin(),arr2.end(),x))<<"th index in the array"<<std::endl;
    std:: cout <<"uper bound it is the "<<distance(arr2.begin(),std::upper_bound(arr2.begin(),arr2.end(),x))<<"th index in the array"<<std::endl;
    return 0;
}
