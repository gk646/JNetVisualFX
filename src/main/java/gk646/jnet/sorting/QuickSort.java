package gk646.jnet.sorting;

public final class QuickSort {
    private QuickSort() {
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivotPos = partition(arr, start, end);

        quickSort(arr, start, pivotPos - 1);
        quickSort(arr, pivotPos + 1, end);
    }

    private static int partition(int[] elements, int left, int right) {
        int pivot = elements[right];
        int i = left;
        int j = right - 1;
        while (i < j) {
            while (i < right && elements[i] < pivot) {
                i++;
            }

            while (j >= i && elements[j] >= pivot) {
                j--;
            }

            if (i < j) {
                int temp = elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }
        int temp = elements[i];
        elements[i] = elements[right];
        elements[right] = temp;

        return i;
    }
}
