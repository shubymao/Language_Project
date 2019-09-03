
public class lcs {
    /**
     * This Function finds the longest common substring for given two strings.
     * @param text1
     * @param text2
     * @return the common substring
     */
    public String find(String text1, String text2) {
        int [][] dp = new int[text1.length()+1][text2.length()+1];
        for(int i=0;i<text1.length();i++){
            for(int j=0;j<text2.length();j++){
                if(text1.charAt(i)==text2.charAt(j))dp[i+1][j+1]=dp[i][j]+1;
                else {
                    dp[i+1][j+1]=Math.max(dp[i+1][j],dp[i][j+1]);
                }
            }
        }
        int i = text1.length(),j=text2.length();
        String res = "";
        while(i>0&&j>0){
            if(text1.charAt(i-1)==text2.charAt(j-1)){
                res = text1.charAt(i-1)+res;
                i--;
                j--;
            }
            else if(dp[i-1][j]>dp[i][j-1]){
                i--;
            }
            else {
                j--;
            }
        }
        return res;
    }
}
