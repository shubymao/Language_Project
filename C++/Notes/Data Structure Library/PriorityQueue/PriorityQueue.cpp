#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include<functional>
// version 2 side req
// int compare(int a,int b){
//     //small to big 
//     return a>b;
// }
int main() {
    std::vector<int> arr={1,4,6,8,9,7,5,2,12};
    //version 1
    //Big to small
    // std::priority_queue<int> q;
    //version 2- Custom comparator
    // std::priority_queue<int,std::vector<int>,std::function<bool(int,int)>> q(compare);
    //version 3 - short version
    auto cmp=[](int a, int b){
        return a>b;
    };
    std::priority_queue<int,std::vector<int>,decltype(cmp)> q(cmp);

    for(int i: arr)q.push(i);
    while(!q.empty()){
        std::cout<<q.top()<<" ";
        q.pop();
    }
    std::cout<<std::endl;
    std::sort(arr.begin(),arr.end(),[](int a, int b){
        //small to big
        return a<b;
    });
    for(int i: arr)std::cout<<i<<" ";
}