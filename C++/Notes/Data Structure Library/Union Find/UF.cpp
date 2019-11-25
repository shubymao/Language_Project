#include <iostream>
#include <vector>
#include <algorithm>
#include<functional>
#include<map>
using namespace std;
/**
Usage shown below
Run result:
    The current UF is {[0], [1], [2], [3], [4], [5], [6], [7], [8], [9]}
    Unioning 2 and 3 
    The current UF is {[0], [1], [2, 3], [4], [5], [6], [7], [8], [9]}
    Unioning 4 and 6 
    The current UF is {[0], [1], [2, 3], [5], [4, 6], [7], [8], [9]}
    Unioning 2 and 7 
    The current UF is {[0], [1], [5], [4, 6], [2, 3, 7], [8], [9]}
    Unioning 4 and 9 
    The current UF is {[0], [1], [5], [2, 3, 7], [8], [4, 6, 9]}
    Unioning 0 and 8 
    The current UF is {[1], [5], [2, 3, 7], [0, 8], [4, 6, 9]}
    Unioning 1 and 5 
    The current UF is {[1, 5], [2, 3, 7], [0, 8], [4, 6, 9]}
    Unioning 2 and 9 
    The current UF is {[1, 5], [0, 8], [2, 3, 4, 6, 7, 9]}
    Unioning 1 and 8 
    The current UF is {[0, 1, 5, 8], [2, 3, 4, 6, 7, 9]} 
 **/
class UF {
    private:
    vector<int> f; 
    public: 
    UF(int n){
        f.resize(n,0);
        for(int i = 0;i< f.size();i++){
            f[i]=i;
        }
    }
    void prettyPrint(){
        cout<<"The current UF is {";
        map<int,vector<int>> mp;
        for(int i = 0;i<f.size();i++){
            find(i);
            mp[f[i]].push_back(i);
        }
        for(auto it = mp.begin();it!=mp.end();it++){
            cout<<"[";
            for(int i=0; i<it->second.size();i++){
                cout<<it->second[i];
                if(i!=it->second.size()-1)cout<<", ";
            }
            cout<<"]";
            if(next(it)!=mp.end())cout<<", ";
        }
        cout<<"}"<<endl;
    }
    int find(int x){
        if(f[x]==x)return f[x];
        //Path compression
        f[x]=find(f[x]);
        //return find
        return f[x];
    }
    bool uni(int x, int y){
        cout<<"Unioning "<<x<<" and "<<y<<" "<<endl;
        if(x>=f.size()||y>=f.size())return false;
        int l= find(x);
        int r= find(y);
        if(l!=r){
            f[l]=r;
        }
        return true;
    }
};
int main(){
    UF* uf = new UF(10);
    uf->prettyPrint();
    uf->uni(2,3);
    uf->prettyPrint();
    uf->uni(4,6);
    uf->prettyPrint();
    uf->uni(2,7);
    uf->prettyPrint();
    uf->uni(4,9);
    uf->prettyPrint();
    uf->uni(0,8);
    uf->prettyPrint();
    uf->uni(1,5);
    uf->prettyPrint();
    uf->uni(2,9);
    uf->prettyPrint();
    uf->uni(1,8);
    uf->prettyPrint();
    return 0;
}