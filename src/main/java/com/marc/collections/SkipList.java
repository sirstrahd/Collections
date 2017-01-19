package com.marc.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Rough SkipList implementation, done for learning purposes.
 * @author sirstrahd
 *
 * @param <T> the Generic Type to use
 */
public class SkipList<T extends Comparable<? super T>> implements Collection<T> {

	private static final int DEFAULT_AMOUNT_OF_ROWS = 4;
	
	private final int rowsAmount;

	public final Random random;

	/**
	 * Auxiliary structure used for containing the elements
	 * @author sirstrahd
	 *
	 * @param <T>
	 */
	private static class SkipListElement<T> {
		private T value;
		private SkipListElement down;
		private SkipListElement right;

		private SkipListElement(T value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value.toString();
		}

	}

	private SkipListElement<T>[] listHeads;
	
	/**
	 * Instantiates a SkipList with the default amount of rows and a generic random number generator.
	 */
	public SkipList() {
		this(DEFAULT_AMOUNT_OF_ROWS);
	}
	
	/**
	 * Instantiates a SkipList with the given amount of rows and a generic random number generator.
	 * @param amount the amount of rows to use. More rows mean more used space but better times.
	 */
	public SkipList(final int amount) {
		this(amount, new Random());
	}

	/**
	 * Instantiates a SkipList with the given amount of rows and the given Random Number generator
	 * @param rowsAmount the amount of rows to use. More rows mean more used space but better times.
	 * @param random a random number generator
	 */
	public SkipList(final int rowsAmount, final Random random) {
		this.rowsAmount = rowsAmount;
		this.random = random;
		listHeads = new SkipListElement[rowsAmount];
	}

	@Override
	public boolean add(final T insertionValue) {
		SkipListElement<?>[] smallerElements = getSmallerElementsArray(insertionValue, false);
		int randomValue = random.nextInt(100);
		final int listsToFill = randomValue / (100 / rowsAmount) + 1;
		for (int i = 0; i < listsToFill; i++) {
			if (smallerElements[i] == null) {
				SkipListElement<T> oldHead = listHeads[i];
				listHeads[i] = new SkipListElement<T>(insertionValue);
				listHeads[i].right = oldHead;
				if (i > 0) {
					listHeads[i].down = listHeads[i - 1];
				}
			} else {
				SkipListElement previousNext = smallerElements[i].right;
				SkipListElement thisElement = new SkipListElement(insertionValue);
				smallerElements[i].right = thisElement;
				thisElement.right = previousNext;
				if (i > 0) {
					thisElement.down = smallerElements[i - 1];
				}
			}
		}
		return true;
	}

	@Override
	public boolean remove(final Object o) {
		final T element = (T) o;
		final SkipListElement<?>[] smallerElements = getSmallerElementsArray((T) o, true);
		boolean modified = false;
		for (int i = 0; i < smallerElements.length; i++) {
			if (listHeads[i] != null && listHeads[i].value.equals(element)) {
				listHeads[i] = listHeads[i].right;
			} else {
				final SkipListElement<?> skipListElement = smallerElements[i];
				if (skipListElement != null) {
					if (skipListElement.equals(listHeads[i])) {
						skipListElement.right = skipListElement.right.right;
						modified = true;
					}
				}
			}
		}
		return modified;
	}

	private SkipListElement<?>[] getSmallerElementsArray(final T insertionValue, final boolean strictlySmaller) {
		int currentList = rowsAmount - 1;
		SkipListElement<?>[] smallerElements = new SkipListElement[rowsAmount];
		while (currentList >= 0) {
			SkipListElement<T> startingPointForThisList;
			if ((currentList < rowsAmount - 1) && (currentList > 0) && smallerElements[currentList + 1] != null) {
				startingPointForThisList = (SkipListElement<T>) smallerElements[currentList + 1].down;
			} else {
				startingPointForThisList = (SkipListElement<T>) listHeads[currentList];
			}
			SkipListElement<?> element = getSmallerElementFromList(startingPointForThisList, insertionValue,
					strictlySmaller);
			if (element != null) {
				smallerElements[currentList] = element;
			}
			currentList--;
		}
		return smallerElements;
	}

	@Override
	public boolean contains(final Object o) {
		return findElement((T) o) != null;
	}

	/**
	 * 
	 * @param position
	 *            the position on the full list
	 * @return the element in position position.
	 */
	public T getByPosition(final int position) {
		long index = 0L;
		SkipListElement current = listHeads[0];
		while (index < position) {
			current = current.right;
			index++;
		}
		return (T) current.value;
	}

	/**
	 * @return the amount of real elements on the SkipList
	 */
	@Override
	public int size() {
		int size = 0;
		SkipListElement current = listHeads[0];
		while (current != null) {
			current = current.right;
			size++;
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		return listHeads[0] == null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object elem : c) {
			if (!contains(elem)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean modified = false;
		for (Object elem : c) {
			modified = add((T) elem) || modified;
		}
		return modified;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (Object elem : c) {
			modified = remove((T) elem) || modified;
		}
		return modified;
	}

	@Override
	public void clear() {
		listHeads = new SkipListElement[rowsAmount];
	}

	private SkipListElement<T> getSmallerElementFromList(SkipListElement<T> skipListElement, T inputValue,
			final boolean strictlySmaller) {
		SkipListElement<T> previousSkipListCandidate = null;
		while (skipListElement != null) {
			if (inputValue.compareTo((T) skipListElement.value) > 0) {
				previousSkipListCandidate = skipListElement;
			} else if (!strictlySmaller && inputValue.compareTo((T) skipListElement.value) == 0) {
				previousSkipListCandidate = skipListElement;
			}
			skipListElement = skipListElement.right;
		}
		return previousSkipListCandidate;
	}

	private SkipListElement<?> findElement(T value) {
		int currentList = rowsAmount - 1;
		SkipListElement<?> foundElement = null;
		SkipListElement<T> startingPointForThisList = (SkipListElement<T>) listHeads[currentList];
		while (foundElement == null && currentList-- >= 0) {
			SkipListElement<?> element = getSmallerElementFromList(startingPointForThisList, value, false);
			if (element != null) {
				if (value.equals(element.value)) {
					foundElement = element;
				} else {
					startingPointForThisList = element.down;
				}
			} else {
				if (currentList >= 0) {
					startingPointForThisList = (SkipListElement<T>) listHeads[currentList];
				}
			}
		}
		return foundElement;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("skipList[\n");
		for (int i = 0; i < listHeads.length; i++) {
			SkipListElement<T> skipListElement = listHeads[i];
			sb.append(" [currentLevel=").append(Integer.toString(i)).append("][");
			while (skipListElement != null) {
				sb.append(skipListElement).append(",");
				skipListElement = skipListElement.right;
			}
			sb.append("]\n");
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
