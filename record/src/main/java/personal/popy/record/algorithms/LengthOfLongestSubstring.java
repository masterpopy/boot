package personal.popy.record.algorithms;

public class LengthOfLongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        int length = s.length();
        int[] chars = new int[256];
        int max = 0;
        int start = 0;
        int count = 0;
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            int pos = chars[c];
            chars[c] = i + 1;
            if (pos != 0 && start < pos) {
                start = pos;
                if (count > max) max = count;
                count = i - start + 1;
                continue;
            }
            count++;
        }
        return count > max ? count : max;
    }
}
