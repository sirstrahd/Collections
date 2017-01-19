package com.marc.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.marc.collections.SkipList;

public class SkipListTest {

	private Random random = new Random(5342342L);

	@Test
	public void add_degenerateTest1() {
		SkipList<Integer> mySkipList = new SkipList<>(1, random);
		mySkipList.add(5);
		assertEquals(1, mySkipList.size());
		assertEquals(new Integer(5), mySkipList.getByPosition(0));
	}

	@Test
	public void add_degenerateTest2() {
		SkipList<Integer> mySkipList = new SkipList<>(1, random);
		mySkipList.add(5);
		mySkipList.add(6);
		mySkipList.add(8);
		mySkipList.add(7);
		assertEquals(4, mySkipList.size());
		assertEquals(new Integer(7), mySkipList.getByPosition(2));
	}

	@Test
	public void add_fakeRandomTest1() {
		SkipList<Integer> mySkipList = new SkipList<>(4, random);
		mySkipList.add(5);
		assertEquals(1, mySkipList.size());
		assertEquals(new Integer(5), mySkipList.getByPosition(0));
	}

	@Test
	public void add_fakeRandomTest2() {
		SkipList<Integer> mySkipList = new SkipList<>(4, random);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(8));
		mySkipList.add(new Integer(7));
		assertEquals(4, mySkipList.size());
		assertEquals(new Integer(7), mySkipList.getByPosition(2));
	}

	@Test
	public void add_realTest3_realRandom() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(8));
		mySkipList.add(new Integer(7));
		assertEquals(4, mySkipList.size());
	}

	@Test
	public void add_duplicates() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(8));
		mySkipList.add(new Integer(7));
		mySkipList.add(new Integer(7));
		assertEquals(5, mySkipList.size());
		assertEquals(new Integer(7), mySkipList.getByPosition(2));
		assertEquals(new Integer(7), mySkipList.getByPosition(3));
	}

	@Test
	public void get_Present() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(8));
		mySkipList.add(new Integer(7));
		assertTrue(mySkipList.contains(new Integer(6)));
	}

	@Test
	public void get_NotPresent() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(10));
		mySkipList.add(new Integer(7));
		assertFalse(mySkipList.contains(new Integer(8)));
	}

	@Test
	public void isEmpty_true() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		assertTrue(mySkipList.isEmpty());
	}

	@Test
	public void isEmpty_false() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		assertFalse(mySkipList.isEmpty());
	}

	@Test
	public void clear_emptyAfterwards() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.clear();
		assertTrue(mySkipList.isEmpty());
	}

	@Test
	public void remove_emptyAfterwards() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.remove(new Integer(5));
		assertTrue(mySkipList.isEmpty());
	}

	@Test
	public void remove_normalCase() {
		SkipList<Integer> mySkipList = new SkipList<>(4);
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(15));
		mySkipList.add(new Integer(17));
		mySkipList.remove(new Integer(5));
		assertEquals(3, mySkipList.size());
		assertEquals(new Integer(17), mySkipList.getByPosition(2));
	}

	@Test
	public void removeAll_removeAllElements() {
		SkipList<Integer> mySkipList = new SkipList<>();
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(15));
		mySkipList.add(new Integer(17));
		List<Integer> elementsToRemove = new ArrayList<>();
		elementsToRemove.add(new Integer(5));
		elementsToRemove.add(new Integer(6));
		elementsToRemove.add(new Integer(15));
		elementsToRemove.add(new Integer(17));
		mySkipList.removeAll(elementsToRemove);
		assertTrue(mySkipList.isEmpty());
	}
	
	@Test
	public void removeAll_removeSome() {
		SkipList<Integer> mySkipList = new SkipList<>();
		mySkipList.add(new Integer(5));
		mySkipList.add(new Integer(6));
		mySkipList.add(new Integer(15));
		mySkipList.add(new Integer(17));
		List<Integer> elementsToRemove = new ArrayList<>();
		elementsToRemove.add(new Integer(5));
		elementsToRemove.add(new Integer(6));
		mySkipList.removeAll(elementsToRemove);
		assertFalse(mySkipList.isEmpty());
		assertEquals(new Integer(15), mySkipList.getByPosition(0));
	}
	
	@Test
	public void addAll_addSome() {
		SkipList<Integer> mySkipList = new SkipList<>();
		List<Integer> elementsToAdd = new ArrayList<>();
		elementsToAdd.add(new Integer(6));
		elementsToAdd.add(new Integer(5));
		mySkipList.addAll(elementsToAdd);
		assertEquals(2, mySkipList.size());
		assertEquals(new Integer(5), mySkipList.getByPosition(0));
	}
}
