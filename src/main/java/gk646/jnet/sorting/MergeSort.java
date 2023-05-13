package gk646.jnet.sorting;

import java.util.Arrays;

public final class MergeSort {

    private MergeSort() {
    }

    public static int[] mergeSort(int[] arr) {
        if (arr.length <= 1) {
            return arr;
        }
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        left = mergeSort(left);
        right = mergeSort(right);

        return mergeArrays(left, right);
    }

    private static int[] mergeArrays(int[] a, int[] b) {
        int[] temp = new int[a.length + b.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                temp[k] = a[i];
                i++;
            } else {
                temp[k] = b[j];
                j++;
            }
            k++;
        }
        while (i < a.length) {
            temp[k] = a[i];
            i++;
            k++;
        }

        while (j < b.length) {
            temp[k] = b[j];
            j++;
            k++;
        }
        return temp;
    }
}
