package com.tech.heathcilff.androidlib.utils;

import android.support.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * Created by lulingzhi on 16/3/25.
 */
public class CollectionUtil {
	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isEmpty(float[] arr) {
		return arr == null || arr.length == 0;
	}

	public static <T> boolean isEmpty(T[] arr) {
		return arr == null || arr.length == 0;
	}

	@SafeVarargs
	public static <T> List<T> concat(List<T>... lists) {
		return new ConcatList<>(lists);
	}

	/**
	 * 获取Y的最大和最小值
	 */

	public static float getArrayMax(float[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}

		float max = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			max = max >= arr[i] ? max : arr[i];
		}
		return max;
	}

	public static float getArrayMin(float[] arr) {

		if (arr == null || arr.length == 0) {
			return 0;
		}

		float min = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			min = min <= arr[i] ? min : arr[i];
		}
		return min;
	}

	/**
	 * a unmodifiable List that warping a seq of list by order
	 *
	 * @param <T>
	 */
	public static class ConcatList<T> extends AbstractList<T> {

		private final List<T>[] lists;

		@SafeVarargs
		public ConcatList(List<T>... lists) {
			this.lists = lists;
		}

		@Override
		public T get(int index) {
			int sIndex = index, size;
			for (List<T> list : lists) {
				if (list == null) {
					continue;
				}
				size = list.size();
				if (sIndex < size) {
					return list.get(sIndex);
				} else {
					sIndex -= size;
				}
			}
			throw new IndexOutOfBoundsException();
		}

		@Override
		public int size() {
			int sum = 0;
			for (List<T> list : lists) {
				sum += list == null ? 0 : list.size();
			}
			return sum;
		}
	}

	/**
	 * @return 最近触摸点index xc位置或比xc稍大的位置
	 */
	public static int getNearestIndex(float[] xArr, float xc) {

		float dX;
		int index = Arrays.binarySearch(xArr, xc);// xc位置或比xc稍大的位置
		if (index < 0) {
			index = -index - 1;
		}
		if (index == 0) {
			return 0;
		} else if (index == xArr.length) {
			return xArr.length - 1;
		} else {
			dX = xArr[index] - xc - xc + xArr[index - 1];
			if (dX >= 0) {
				index--;
			}
			return index;
		}
	}

	/**
	 * 去重添加,并保持顺序
	 * 重复的item将替换,不重复的添加到末尾
	 * item 须保证其hashCode&equals可靠
	 *
	 * @param items     源列表
	 * @param moreItems 新加列表
	 * @param <T>       类型
	 */
	public static <T> void deduplication(@NonNull List<T> items, @NonNull List<T> moreItems) {
		List<T> temp = new ArrayList<>(moreItems);
		for (ListIterator<T> li = items.listIterator(); li.hasNext(); ) {
			T next = li.next();
			int i = temp.indexOf(next);
			if (i >= 0) {
				T t = temp.remove(i);
				li.set(t);
			}
		}
		items.addAll(temp);
	}

	/**
	 * 最大值
	 */
	private float getMax(float[] arr) {
		if (arr == null || arr.length <= 0) {
			return -1;
		}

		float max = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			max = max >= arr[i] ? max : arr[i];
		}
		return max;
	}

	/**
	 * 最小值
	 */
	private float getMin(float[] arr) {
		if (arr == null || arr.length <= 0) {
			return -1;
		}

		float min = arr[0];
		for (int i = 1, len = arr.length; i < len; i++) {
			min = min <= arr[i] ? min : arr[i];
		}
		return min;
	}
	
	@SuppressWarnings("unchecked")
	public static<E> Iterator<E> emptyIterator(){
		return (Iterator<E>) EmptyIterator.EMPTY_ITERATOR;
	}

	/**
	 * Created by lulingzhi on 16/7/28.
	 * List->Map converter
	 */
	public static interface LMConverter<E, K, V> {
		/**
		 * key from list item will put into map
		 *
		 * @param item list item
		 * @return key of map to put in
		 */
		K getKey(E item);

		/**
		 * value of list item will put into map
		 *
		 * @param item list item
		 * @return value of map to put in
		 */
		V getValue(E item);
	}
	
	private static class EmptyIterator<E> implements Iterator<E> {
		static final EmptyIterator<Object> EMPTY_ITERATOR
				= new EmptyIterator<>();
		
		public boolean hasNext() { return false; }
		public E next() { throw new NoSuchElementException(); }
		public void remove() { throw new IllegalStateException(); }
	}
}
