package personal.popy.record.algorithms;

public class Questions {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        public void print() {

            ListNode l = this;
            while (l != null) {
                System.out.print(l.val);
                System.out.print(" ");
                l = l.next;
            }
        }


    }

    public static ListNode fromString(String str) {
        String[] strings = str.split(",");
        ListNode node = new ListNode(0);
        ListNode head = node;
        for (String s : strings) {
            node.next = new ListNode(Integer.parseInt(s));
            node = node.next;
        }
        return head.next;
    }

    //19.给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    //将两个有序链表合并为一个新的有序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        ListNode head = l1;
        while (true) {
            ListNode next = l1.next;
            ListNode nextv;
            if (l1.val >= l2.val) {
                int v = l2.val;
                l2.val = l1.val;
                l1.val = v;
                nextv = l2;
            } else {
                while (l1.next != null && l1.next.val < l2.val) {
                    l1 = l1.next;
                }
                next = l1.next;
                nextv = next;
            }

            l1.next = l2;
            l2 = l2.next;
            l1.next.next = next;

            if (nextv == null) {
                l1.next.next = l2;
                break;
            }
            if (l2 == null) {
                break;
            }
            l1 = nextv;
        }

        return head;
    }
}
