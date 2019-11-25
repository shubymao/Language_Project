#include <iostream>
#include <map>
#include <unordered_map>
#include <vector>
#include <string>
int main(){
    //hash map 
    std::unordered_map<int,std::string> mp;
    mp.insert(std::pair<int,std::string>(0,"ZERO"));
    mp[1] = "ONE";
    //iterate through the map the order will not always be same as insert
    for (auto it = mp.begin();it!=mp.end();it++){
        //using pointer to get the item
        //first is key
        //second is value
        std::cout<<it->first<< " = "<< it->second <<std::endl;
    }
    //at get the value and allow setting
    mp.at(0) = "NEW ZERO";
    std::cout<< mp[0]<<std::endl;
    //normal map with binary tree structure 
    // std::map<int,std::string> mp;
    // mp[3]="Three";
    // mp.insert(std::pair<int,std::string>(0,"ZERO"));
    // mp[1] = "ONE";
    // //iterate through the map the map will be in order 1,2,3,....
    // for (auto it = mp.begin();it!=mp.end();it++){
    //     std::cout<<it->first<< " = "<< it->second <<std::endl;
    // }
}
