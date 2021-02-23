public class upper_lower_bound {
    public static void main(String[] args) {
        int[] arr = {0,1,2,3,4,5,5,5,6,7, 9};
        int[] ind = {0,1,2,3,4,5,6,7,8,9,10}; // Used for reference
        
        // target lower than smallest item
        System.out.println("Searching for -1 in arr.");
        System.out.println(lower_bound(arr,-1)); // both return 0 
        System.out.println(upper_bound(arr,-1));
        
        // target higher than largest item
        System.out.println("Searching for 15 in arr.");
        System.out.println(lower_bound(arr,15)); // both return len(arr) = 11
        System.out.println(upper_bound(arr,15)); // 
        
        // only 'one' target exist in the search array
        System.out.println("Searching for 3 in arr.");
        System.out.println(lower_bound(arr,3)); // returns target index (3)
        System.out.println(upper_bound(arr,3)); // returns target index + 1 (4)
        
        // target is within bound of max and min but doesn't exist in the array
        // Both would returns where the target index would be if inserted (10)
        System.out.println("Searching for 8 in arr.");
        System.out.println(lower_bound(arr,8)); 
        System.out.println(upper_bound(arr,8)); 
        
        // target exist multiple time within the array
        System.out.println("Searching for 5 in arr.");
        System.out.println(lower_bound(arr,5)); // return first instance index (5)
        System.out.println(upper_bound(arr,5)); // return last instance index + 1 (8)
        
    }
    
    public static int lower_bound(int[] arr, int target){
        int l = 0;
        int r = arr.length; 
        while(l < r){
            int mid = (l+r)>>>1;
            if(arr[mid] >= target) r = mid;
            else l = mid + 1;
        }
        return r;
    }
    
    public static int upper_bound(int[] arr, int target){
        int l = 0;
        int r = arr.length;
        while(l < r){
            int mid = (l+r)>>>1;
            if(arr[mid] <= target) l = mid + 1;
            else r = mid;
        }
        return r;
    }
}