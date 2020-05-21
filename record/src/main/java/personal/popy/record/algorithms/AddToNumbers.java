package personal.popy.record.algorithms;

public class AddToNumbers {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = null;
        ListNode next = null;
        while (l1 != null || l2 != null) {
            if (result == null) {
                result = new ListNode(0);
                next = result;
            } else {
                if (next.next == null) {
                    next.next = new ListNode(0);
                }
                next = next.next;
            }
            int i1, i2;
            if (l1 == null) {
                i1 = 0;
            } else {
                i1 = l1.val;
                l1 = l1.next;
            }
            if (l2 == null) {
                i2 = 0;
            } else {
                i2 = l2.val;
                l2 = l2.next;
            }
            int value = i1 + i2 + next.val;
            if (value >= 10) {
                next.next = new ListNode(1);
                value -= 10;
            }
            next.val = value;
        }
        return result;
    }
}
