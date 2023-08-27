package edu.iastate.cs228.hw2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

class TestCases {

	int testCases = 10000;
	PrimeFactorization test;
	PrimeFactorization test2;
	PrimeFactorization test3;
	PrimeFactorization test4;
	String expected;
	int a = 0;
	int b = 0; 
	Random rand = new Random();

	@Test
	void primeFactorTest() {
		PrimeFactor first = new PrimeFactor(3, 2);
		assertEquals(3, first.prime);
		PrimeFactor second = first.clone();
		assertEquals(3, second.prime);
		assertEquals(2, second.multiplicity);

	}
	
	@Test
	void OverflowTest() {
		long max = 9223372036854775807L; //"L is needed to indicate to the compiler that the number is of type long and not type int" -stolen from Ashwin Jacob (aka BlitzDestroyer on discord) blatantly
		test = new PrimeFactorization(max);
		test.add(5, 3);
		assertEquals(-1, test.value());
	}

	@Test
	void primeFactorizationBasicsTest() {
		test = new PrimeFactorization();
		test.add(2, 1);
		test.add(3, 2);
		test.add(17, 1);
		test.remove(2, 1);
		test.add(19, 1);
		assertEquals(3, test.size());
		assertEquals(2907, test.value());
	}

	@Test
	void constructorLongTest() {
		PrimeFactorization test = new PrimeFactorization(5814);
		assertEquals(4, test.size());
	}

	@Test
	void constructorListTest() {
		PrimeFactor[] testList = new PrimeFactor[4];
		testList[0] = new PrimeFactor(2, 1);
		testList[1] = new PrimeFactor(3, 2);
		testList[2] = new PrimeFactor(17, 1);
		testList[3] = new PrimeFactor(19, 1);

		PrimeFactorization test = new PrimeFactorization(testList);
		assertEquals(4, test.size());
		assertEquals(5814, test.value());
		expected = "2 * 3^2 * 17 * 19";
		assertEquals(expected, test.toString());
	}

	@Test
	void cloneConstructorTest() {
		PrimeFactorization test = new PrimeFactorization();
		test.add(2, 1);
		test.add(7, 2);
		assertEquals(2, test.size());
		PrimeFactorization test2 = new PrimeFactorization(test);
		assertEquals(2, test2.size());
	}

	@Test
	void isPrimeTest() {
		assertFalse(PrimeFactorization.isPrime(1));
		assertTrue(PrimeFactorization.isPrime(2));
		assertTrue(PrimeFactorization.isPrime(3));
		assertFalse(PrimeFactorization.isPrime(4));
		assertTrue(PrimeFactorization.isPrime(5));
		assertFalse(PrimeFactorization.isPrime(6));
		assertTrue(PrimeFactorization.isPrime(7));
		assertFalse(PrimeFactorization.isPrime(8));
		assertFalse(PrimeFactorization.isPrime(9));
		assertFalse(PrimeFactorization.isPrime(10));
	}

	@Test
	void toStringTest() {
		expected = "2 * 3^2 * 17 * 19";
		test = new PrimeFactorization(5814);
		assertTrue(test.toString().equals(expected));
	}

	@Test
	void addTest() {
		test = new PrimeFactorization(6);
		test.add(5, 2);
		assertEquals(3, test.size());
		expected = "2 * 3 * 5^2";
		assertEquals(expected, test.toString());
		test = new PrimeFactorization(10);
		test.add(3, 3);
		assertEquals(3, test.size());
		expected = "2 * 3^3 * 5";
		
	}

	@Test
	void multiplyStaticTest() {
		for (int i = 0; i < testCases; i++) {
			a = rand.nextInt(10000);
			b = rand.nextInt(10000);
			a++;
			b++;
			test = new PrimeFactorization(a);
			test2 = new PrimeFactorization(b);
			//test3 = PrimeFactorization.multiply(test, test2);
			assertTrue(test3.value() == a * b);
		}
	}

	@Test
	void multiplyLongTest() {
		a=1;
		b=1;
		test = new PrimeFactorization(a);
		test.multiply(b);
		assertTrue(test.value() == a * b);
		a=1;
		b=5;
		test = new PrimeFactorization(a);
		test.multiply(b);
		assertTrue(test.value() == a * b);
		
		//mass test
		for (int i = 0; i < testCases; i++) {
			a = rand.nextInt(10000);
			b = rand.nextInt(10000);
			a++;
			b++;
			test = new PrimeFactorization(a);
			test.multiply(b);
			assertTrue(test.value() == a * b);
		}
	}

	@Test
	void multiplyPFTest() {
		for (int i = 0; i < testCases; i++) {
			a = rand.nextInt(10000);
			b = rand.nextInt(10000);
			a++;
			b++;
			test = new PrimeFactorization(a);
			test2 = new PrimeFactorization(b);
			test.multiply(test2);
			assertTrue(test.value() == a * b);
		}
	}

	//recommend that you use large number of test cases for division
	@Test
	void divisionFringeTest() {
		a = 62;
		b = 1;
		test = new PrimeFactorization(a);
		assertTrue(test.dividedBy(b));
		assertEquals(a, test.value());
		a = 1;
		b = 1;
		test = new PrimeFactorization(a);
		assertTrue(test.dividedBy(b));
		assertEquals(a, test.value());
		
	}
	
	@Test
	void divisionAutomatedPFTest() {
		for (int i = 0; i < testCases; i++) {
			a = rand.nextInt(10000);
			b = rand.nextInt(10000);
			a ++;
			b ++;
			test = new PrimeFactorization(a);
			test2 = new PrimeFactorization(b);
			if (a % b == 0 && b!= 1) {
				assertTrue(test.dividedBy(test2));
				assertTrue(a/b == test.value());
			} else  {
				assertFalse(test.dividedBy(test2));
			}
		}
	}
	
	@Test
	void divisionAutomatedLongTest() {
		for (int i = 0; i < testCases; i++) {
			a = rand.nextInt(10000);
			b = rand.nextInt(10000);
			a ++;
			b ++;
			a=537;
			b=13;
			test = new PrimeFactorization(a);
			if (a % b == 0) {
				assertTrue(test.dividedBy(b));
				assertTrue(a/b == test.value());
			} else  {
				assertFalse(test.dividedBy(b));
			}
		}
	}
	
	//need to expand this
	@Test
	void EuclideanTest() {
		assertEquals(23, PrimeFactorization.Euclidean(184, 69));
	}
	
	@Test
	void gcdLongTest() {
		a = 184;
		b = 69;
		test = new PrimeFactorization(a);
		test2 = new PrimeFactorization(test.gcd(b));
		assertEquals(23, test2.value());
		a = 123;
		b = 410;
		test = new PrimeFactorization(a);
		test2 = new PrimeFactorization(test.gcd(b));
		assertEquals(41, test2.value());
		a = 1;
		b = 1;
		test = new PrimeFactorization(a);
		test2 = new PrimeFactorization(test.gcd(b));
		assertEquals(1, test2.value());
		
		
	}
	
	@Test
	void gcdPFTest() {
		a = 184;
		b = 69;
		test = new PrimeFactorization(a);
		test2 = new PrimeFactorization(b);
		test3 = new PrimeFactorization(test.gcd(test2));
		
		assertEquals(23, test3.value());
	}
	
	@Test
	void lcmPFtest() {
		test = new PrimeFactorization(12);
		test2 = new PrimeFactorization(42);
		test3 = test.lcm(test2);
		assertEquals(84, test3.value());
		test = new PrimeFactorization(13);
		test2 = new PrimeFactorization(25);
		test3 = test.lcm(test2);
		assertEquals(325, test3.value());
		expected = "5^2 * 13";
		assertEquals(expected, test3.toString());
	}

}
