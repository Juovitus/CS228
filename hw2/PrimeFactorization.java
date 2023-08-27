package edu.iastate.cs228.hw2;

/**
 *  
 * @author Noah Hoss
 *
 */

import java.util.ListIterator;

public class PrimeFactorization implements Iterable<PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; // the factored integer
						// it is set to OVERFLOW when the number is greater than 2^63-1, the
						// largest number representable by the type long.

	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;

	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;

	private int size; // number of distinct prime factors

	private PrimeFactor primeFactor; // We'll be needing this!(...I think)

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor constructs an empty list to represent the number 1.
	 * 
	 * Combined with the add() method, it can be used to create a prime
	 * factorization.
	 */
	public PrimeFactorization() {
		//Default constructor sets to given.
		this.head = new Node();
		this.tail = new Node();
		link(head, tail); //Link is just setting next and previous for given nodes
		size = 0;
	}

	/**
	 * Obtains the prime factorization of n and creates a doubly linked list to
	 * store the result. Follows the direct search factorization algorithm in
	 * Section 1.2 of the project description.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {
		//Default
		this();
		//Throw is n < 1 as stated.
		if (n < 1) {
			throw new IllegalArgumentException("n is less than one in PrimeFactorization(long n)");
		}
		
		int num2PlaceHolder = 2;
		//int numSq = 4;
		//default multiplicity
		int multiplicity = 0;
		//We'll use current num instead of, and PDF says to use multiplication for efficiency.
		long currentNum = n;
		//Setup iterator
		PrimeFactorizationIterator iterator = new PrimeFactorizationIterator();
		while(currentNum * currentNum <= n) {
			//Check if currentNum is prime
			if(isPrime(currentNum)) {
				//If it is, we can create new PF with new num and 1, cast to int.
				PrimeFactor p = new PrimeFactor((int)currentNum, 1);
				//add it to iterator
				iterator.add(p);
				//break out of loop
				break;
				
			//If num2PlaceHolder is prime then
			}else if(isPrime(num2PlaceHolder)) {
				//check if we have a multiplicity, if we do
				if(multiplicity != 0) {
					//then we create a pf, add to iterator and set multiplicity back to 0 for now.
					PrimeFactor p = new PrimeFactor(num2PlaceHolder, multiplicity);
					iterator.add(p);
					multiplicity = 0;
				}
				
			}else {
				//increment to check next num
				num2PlaceHolder++;
			}
		}
		//update
		updateValue();
	}

	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in
	 * the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		//TODO - double check this works fine(I think it does??)
		// default
		this();
		// set value
		value = pf.value;
		// setup iterators
		PrimeFactorizationIterator iterator = new PrimeFactorizationIterator();
		PrimeFactorizationIterator pfIterator = pf.new PrimeFactorizationIterator();
		// placeholder node
		Node toAdd = null;
		// set cursor
		iterator.cursor = head;
		// loop through
		for (int i = 0; i < pf.size; i++) {
			// if size is 0 we can just exit.
			if (pfIterator.cursor == (pf.tail)) {
				break;
			}
			// set new node value.
			toAdd = new Node(pfIterator.cursor.pFactor);
			// link
			link(iterator.cursor, toAdd);
			// set cursor x2
			iterator.cursor = toAdd;
			pfIterator.cursor = pfIterator.cursor.next;
			// increment
			size++;
		}
		// link cursor and tail
		link(iterator.cursor, tail);
	}

	/**
	 * Constructs a factorization from an array of prime factors. Useful when the
	 * number is too large to be represented even as a long integer.
	 * 
	 * @param pflist
	 */
	public PrimeFactorization(PrimeFactor[] pfList) {
		//TODO - double check this works fine(I think it does??)
		this();
		PrimeFactorizationIterator iter = new PrimeFactorizationIterator();
		// loop through
		for (int i = 0; i < pfList.length; i++) {
			// update, add, increment.
			primeFactor = new PrimeFactor(pfList[i].prime, pfList[i].multiplicity);
			iter.add(primeFactor);
			size++;
		}
		// update
		updateValue();
	}

	// --------------
	// Primality Test
	// --------------

	/**
	 * Test if a number is a prime or not. Check iteratively from 2 to the largest
	 * integer not exceeding the square root of n to see if it divides n.
	 * 
	 * @param n
	 * @return true if n is a prime false otherwise
	 */
	public static boolean isPrime(long n) {
		// default boolean to return.
		//boolean result = false;
		// start at 2, increment through
//		for (int i = 2; i <= n + 1; i++) {
//			if (i == n) {
//				result = true;
//			} else if (n % i == 0) {
//				result = false;
//			}
//		}
		
		//we didn't make use of square root as mentioned we should, so lets rewrite it a bit.
		//No need for an instance boolean variable really. IDK why I made one before.
		for(int i = 2; i <= Math.sqrt(n); i++) {
			if(n % i == 0) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------
	// Multiplication and Division
	// ---------------------------

	/**
	 * Multiplies the integer v represented by this object with another number n.
	 * Note that v may be too large (in which case this.value == OVERFLOW). You can
	 * do this in one loop: Factor n and traverse the doubly linked list
	 * simultaneously. For details refer to Section 3.1 in the project description.
	 * Store the prime factorization of the product. Update value and size.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException("n < 1 in multiply(long n)");
		} else {
			// copys so we don't fudge anything
			PrimeFactorization p = new PrimeFactorization(n);
			PrimeFactorizationIterator iterator = new PrimeFactorizationIterator();
			PrimeFactorizationIterator iteratorV2 = p.new PrimeFactorizationIterator();
			//have these setup
			PrimeFactor curr = iterator.next();
			PrimeFactor newCurr = iteratorV2.next();
			//go back(DO I NEED THESE?)
			iteratorV2.previous();
			iterator.previous();
			
			//If one of them doesn't have a next, we don't need to do anything here
			while(iterator.hasNext() && iteratorV2.hasNext()) {
				//Check if they're equal for starts
				if(curr.prime == newCurr.prime) {
					//If they're equal, we add their multiplicitys and go next
					curr.multiplicity += newCurr.multiplicity;
					curr = iterator.next();
					newCurr = iteratorV2.next();
				}else if(curr.prime < newCurr.prime) {
					//if current prime is less than newCurr prime then we call add with newCurr
					add(newCurr.prime, newCurr.multiplicity);
					newCurr = iteratorV2.next();
				}else {
					//If neither of these are true, we increment curr
					curr = iterator.next();
				}
				//Now we check if iteratorv2 is longer than iterator, if so we add on
				while(iteratorV2.hasNext() && !iterator.hasNext()) {
					//add and set next.
					add(newCurr.prime, newCurr.multiplicity);
					newCurr = iteratorV2.next();
				}
			}
			// update
			updateValue();
		}
	}

	/**
	 * Multiplies the represented integer v with another number in the factorization
	 * form. Traverse both linked lists and store the result in this list object.
	 * See Section 3.1 in the project description for details of algorithm.
	 * 
	 * @param pf
	 */
	public void multiply(PrimeFactorization pf) {
		//TODO check if this works
		// create copys so we don't fudge stuff
		PrimeFactorizationIterator iterator = new PrimeFactorizationIterator();
		//PrimeFactorizationIterator iteratorV2 = pf.new PrimeFactorizationIterator();
		PrimeFactorizationIterator iteratorV2 = pf.iterator(); //Not sure if these work the same or not but we should
															   //Be using iterator of given PF
		// loop through
		for (int i = 0; i < this.size + pf.size; i++) {
			if (iterator.cursor.next == tail && iterator.cursor.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
				// if v2 has more, we add, increment and continue!
				if (iteratorV2.hasNext()) {
					iterator.add(iteratorV2.cursor.pFactor);
					size++;
					iteratorV2.cursor = iteratorV2.cursor.next;
					continue;
				}
				// if v2 c = pf.t we add itv2pf to it and increment.
				if (iteratorV2.cursor.next == pf.tail) {
					iterator.add(iteratorV2.cursor.pFactor);
					size++;
				}
				// break as there's no point
				break;
			}
			// If it prime == itv2 prime then add multiplicities
			if (iterator.cursor.pFactor.prime == iteratorV2.cursor.pFactor.prime) {
				iterator.cursor.pFactor.multiplicity += iteratorV2.cursor.pFactor.multiplicity;
				// go next
				if (iterator.hasNext()) {
					iterator.cursor = iterator.cursor.next;
				}
				// go next
				if (iteratorV2.hasNext()) {
					iteratorV2.cursor = iteratorV2.cursor.next;
				}
			}
			// if it prime < itv2 prime, go next if we can
			if (iterator.cursor.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
				if (iterator.hasNext()) {
					iterator.cursor = iterator.cursor.next;
				}
			}
			// if it prime > itv2 prime, go previous if we can
			if (iterator.cursor.pFactor.prime > iteratorV2.cursor.pFactor.prime) {
				if (iterator.hasPrevious()) {
					// if it prime < itv2 prime then add itv2 pf onto it
					if (iterator.cursor.previous.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
						iterator.add(iteratorV2.cursor.pFactor);
					}
				}
			}
		}
		// update
		updateValue();
	}

	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {
		// create iterators for the two passed in pfs.
		PrimeFactorizationIterator iterator = pf1.iterator();
		PrimeFactorizationIterator iteratorV2 = pf2.iterator();
		// Loop through
		for (int i = 0; i < pf1.size + pf2.size; i++) {
			if (iterator.cursor.next == pf1.tail && iterator.cursor.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
				// If it has more we continue
				if (iteratorV2.hasNext()) {
					iterator.add(iteratorV2.cursor.pFactor);
					pf1.size++;
					iteratorV2.cursor = iteratorV2.cursor.next;
					continue;
				}
				// if next cursor == tail then
				if (iteratorV2.cursor.next == pf2.tail) {
					// add and increase
					iterator.add(iteratorV2.cursor.pFactor);
					pf1.size++;
				}
				// break as there's no point
				break;
			}
			// if the iterators prime is same then we add multiplicitys
			if (iterator.cursor.pFactor.prime == iteratorV2.cursor.pFactor.prime) {
				iterator.cursor.pFactor.multiplicity += iteratorV2.cursor.pFactor.multiplicity;
				// go next
				if (iterator.hasNext()) {
					iterator.cursor = iterator.cursor.next;
				}
				// go next
				if (iteratorV2.hasNext()) {
					iteratorV2.cursor = iteratorV2.cursor.next;
				}
			}
			// if it1 p < it2 p then we go next for it1 if available.
			if (iterator.cursor.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
				if (iterator.hasNext()) {
					iterator.cursor = iterator.cursor.next;
				}
			}
			// if it1 p > it2 p then we go back
			if (iterator.cursor.pFactor.prime > iteratorV2.cursor.pFactor.prime) {
				if (iterator.hasPrevious()) {
					// if it1 p < it2 p then we add it2p to it1
					if (iterator.cursor.previous.pFactor.prime < iteratorV2.cursor.pFactor.prime) {
						iterator.add(iteratorV2.cursor.pFactor);
					}
				}
			}
		}
		// update and return
		pf1.updateValue();
		return new PrimeFactorization(pf1);
	}

	/**
	 * Divides the represented integer v by n. Make updates to the list, value, size
	 * if divisible. No update otherwise. Refer to Section 3.2 in the project
	 * description for details.
	 * 
	 * @param n
	 * @return true if divisible false if not divisible
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("Value of N is <= 0");
		}
		// If value < n and value is not equal to 1 we return false, otherwise we pass
		// and return true.
		if (value < n && value != 1) {
			return false;
		} else {
			PrimeFactorization primeTemp = new PrimeFactorization(n);
			return dividedBy(primeTemp);
		}
	}

	/**
	 * Division where the divisor is represented in the factorization form. Update
	 * the linked list of this object accordingly by removing those nodes housing
	 * prime factors that disappear after the division. No update if this number is
	 * not divisible by pf. Algorithm details are given in Section 3.2.
	 * 
	 * @param pf
	 * @return true if divisible by pf false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {
		//catch these cases where its false easilly
		if((this.value != -1 && pf.value == -1)||(this.value != -1 && pf.value != -1 && this.value < pf.value)) {
			return false;
		}
		//If equal, we clear, set value to 1 as 2/2 = 1 and return true since its divisible.
		else if(this.value == pf.value){
			clearList();
			value = 1;
			return true;
		}
		else{
			PrimeFactorization thisCopy = new PrimeFactorization(this);
			PrimeFactorizationIterator iteratorOfThis = thisCopy.iterator();
			PrimeFactorizationIterator iteratorOfPf = pf.iterator();
			//Check if has next
			while(iteratorOfThis.hasNext()){
				//Check if pf.p is >= itofp.p
				if(iteratorOfThis.cursor.pFactor.prime >= iteratorOfPf.cursor.pFactor.prime){
					//if it is, is it equal to eachother AND multiplicity is less? if so return false
					if((iteratorOfThis.cursor.pFactor.prime == iteratorOfPf.cursor.pFactor.prime) && 
					   (iteratorOfThis.cursor.pFactor.multiplicity < iteratorOfPf.cursor.pFactor.multiplicity)){
						return false;
					}
					//Check if pf.p > itofp.p if so return false
					else if(iteratorOfThis.cursor.pFactor.prime > iteratorOfPf.cursor.pFactor.prime){
						return false;
					}
					//Else we actually do stuff
					else{
						if((iteratorOfThis.cursor.pFactor.prime == iteratorOfPf.cursor.pFactor.prime) && 
						   (iteratorOfThis.cursor.pFactor.multiplicity >= iteratorOfPf.cursor.pFactor.multiplicity)){
							//subtract multiplicitys
							iteratorOfThis.cursor.pFactor.multiplicity -= iteratorOfPf.cursor.pFactor.multiplicity;
							//Since we subtracted, check if it's 0, if it is remove it.
							 if(iteratorOfThis.cursor.pFactor.multiplicity == 0){
								 //remove 
								 iteratorOfThis.remove();
							 }
							 //go next
							 iteratorOfThis.next();
							 iteratorOfPf.next();
						}
					}
				}else if((!iteratorOfThis.hasNext() && iteratorOfPf.hasNext())){
					//If iterator doesn't have next we can exit
					return false;
				}else if(iteratorOfPf.cursor == pf.tail)
				{
					//update and set all variables
					thisCopy.updateValue();
					head = thisCopy.head;
					tail = thisCopy.tail;
					size = thisCopy.size;
					value = thisCopy.value;
				}
			}
			
		}
		//divisible by
		return true;
	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the
	 * object pf2. Return a new object representing the quotient if divisible. Do
	 * not make changes to pf1 and pf2. No update if the first number is not
	 * divisible by the second one.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible null
	 *         otherwise
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		//TODO - check if this works
		// Copy's so we don't change pf1/pf2
		PrimeFactorization pf1Copy = new PrimeFactorization(pf1);
		PrimeFactorization pf2Copy = new PrimeFactorization(pf2);
		// Iterators for the copys
		PrimeFactorizationIterator iterator = pf1Copy.new PrimeFactorizationIterator();
		PrimeFactorizationIterator iterator2 = pf2Copy.new PrimeFactorizationIterator();
		if (!(pf1.value != -1 && pf2.value != -1 && pf1.value <= pf2.value) || !(pf1.value != -1 && pf2.value == -1)) {
			// If they're equal we don't have to do much, awesome!
			if (pf1.value == pf2.value) {
				// return it
				return new PrimeFactorization(pf1Copy);
			} else {
				for (int i = 0; i < pf1.size + pf2.size; i++) {
					if (iterator.cursor.pFactor.prime >= iterator2.cursor.pFactor.prime) {
						if (iterator.cursor.pFactor.prime > iterator2.cursor.pFactor.prime) {
							// Not sure where we'd get here but we should cover it.
							return null;
						}
						if (iterator.cursor.pFactor.prime == iterator2.cursor.pFactor.prime) {
							if (iterator.cursor.pFactor.multiplicity < iterator2.cursor.pFactor.multiplicity) {
								// Not sure where we'd get here but we should cover it.
								return null;
							}
							if (iterator.cursor.pFactor.multiplicity >= iterator2.cursor.pFactor.multiplicity) {
								iterator.cursor.pFactor.multiplicity = iterator.cursor.pFactor.multiplicity
										- iterator2.cursor.pFactor.multiplicity;
								if (iterator.cursor.pFactor.multiplicity == 0) {
									iterator.pending = iterator.cursor;
									iterator.cursor = iterator.cursor.next;
									iterator2.cursor = iterator2.cursor.next;
									iterator.remove();
									pf1Copy.size--;
								}
							}
						}
					}
					// If iterator 1 doesnt have next but 2 does we should return null cuz its
					// messed up
					if (!(iterator.hasNext()) && iterator2.hasNext()) {
						return null;
					}
					// If cursor is at tail we return pf1copy.
					if (iterator2.cursor == pf2Copy.tail) {
						pf1Copy.updateValue();
						return new PrimeFactorization(pf1Copy);
					} else {
						// go next!
						if (iterator.hasNext()) {
							iterator.cursor = iterator.cursor.next;
						}
						// go next!
						if (iterator2.hasNext()) {
							iterator2.cursor = iterator2.cursor.next;
						}
						continue;
					}
				}
			}
		}
		// The method needs this so
		return null;
	}

	// -------------------------------------------------
	// Greatest Common Divisor and Least Common Multiple
	// -------------------------------------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and
	 * an input integer n. Returns the result as a PrimeFactor object. Calls the
	 * method Euclidean() if this.value != OVERFLOW.
	 * 
	 * It is more efficient to factorize the gcd than n, which can be much greater.
	 * 
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException("n < 1 in gcd(long n)");
		}
		if (this.value == OVERFLOW) {
			return null;
		} else {
			// call this because this.value != overflow.
			return new PrimeFactorization(Euclidean(this.value, n));
		}
	}

	/**
	 * Implements the Euclidean algorithm to compute the gcd of two natural numbers
	 * m and n. The algorithm is described in Section 4.1 of the project
	 * description.
	 * 
	 * @param m
	 * @param n
	 * @return gcd of m and n.
	 * @throws IllegalArgumentException if m < 1 or n < 1
	 */
	public static long Euclidean(long m, long n) throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException("m or n < 1 in Euclidean.");
		} else {
			long remainder = 0, gcd = 0, temp = 0;
			if(m < n) {
				remainder = n;
				gcd = m;
			}else if(m > n) {
				remainder = m;
				gcd = n;
			}else {
				remainder = n;
				gcd = m;
			}
			//Keep going until we return
			while(true) {
				//if current gcd if mod is 0
				if(remainder % gcd == 0) {
					return gcd;
				}else {
					//Else we should keep doin more increments
					temp = remainder % gcd;
					remainder = gcd;
					gcd = temp;
				}
			}
		}
	}

	/**
	 * Computes the gcd of the values represented by this object and pf by
	 * traversing the two lists. No direct computation involving value and pf.value.
	 * Refer to Section 4.2 in the project description on how to proceed.
	 * 
	 * @param pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {
		// create instance variables for use
		PrimeFactorizationIterator iterator = iterator();
		PrimeFactorizationIterator iteratorV2 = pf.iterator();
		PrimeFactorization gcd = new PrimeFactorization();
		//we need an iterator for gcd
		PrimeFactorizationIterator gcdIterator = gcd.iterator();
		//Let's add in some instance variables for the PrimeFactors of the current iterator
		PrimeFactor pf1 = iterator.next();
		PrimeFactor pf2 = iteratorV2.next();
		for (int i = 0; i < this.size; i++) {
			//Set pf1 to next iterator pf
			if(i != 0) {
				pf1 = iterator.next();
			}
			for(int n = 0; n < pf.size; n++) {
				//Check if the prime facotrs PF's are equal
				if(pf1.prime == pf2.prime) {
					//If they are, check their multiplicities
					if(pf1.multiplicity < pf2.multiplicity) {
						//If multiplicity of pf1 is less than pf2 then we add on pf1
						gcdIterator.add(pf1);
					}else {
						//else we add on pf2
						gcdIterator.add(pf2);
					}
				}
			}
		}
		// update and return
		gcd.updateValue();
		return gcd;
	}

	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		//This method is bacially the same as previous
		// create instance variables for use.
		PrimeFactorizationIterator iterator = pf1.iterator();
		PrimeFactorizationIterator iteratorV2 = pf2.iterator();
		PrimeFactorization gcd = new PrimeFactorization();
		PrimeFactorizationIterator gcdIterator = gcd.iterator();
		//PFS of iterators, just like in previous gcd
		PrimeFactor primeFactor1 = iterator.next();
		PrimeFactor primeFactor2 = iteratorV2.next();
		for(int i = 0; i < pf1.size; i++){
			if(i != 0) {
				primeFactor1 = iterator.next();
			}
			for(int n = 0; n < pf2.size; n++){
				if(primeFactor1.prime == primeFactor2.prime){
					if(primeFactor1.multiplicity < primeFactor2.multiplicity){
						gcdIterator.add(primeFactor1);
					}else {
						gcdIterator.add(primeFactor2);
					}
				}
				primeFactor2 = iteratorV2.next();
			}
		}
		// update and return
		gcd.updateValue();
		return gcd;
	}

	/**
	 * Computes the least common multiple (lcm) of the two integers represented by
	 * this object and pf. The list-based algorithm is given in Section 4.3 in the
	 * project description.
	 * 
	 * @param pf
	 * @return factored least common multiple
	 */
	public PrimeFactorization lcm(PrimeFactorization pf) {
		// I dont understand the lcm functions. I'm not great at math even tho I've
		// taken through calc 3.
		return null;
	}

	/**
	 * Computes the least common multiple of the represented integer v and an
	 * integer n. Construct a PrimeFactors object using n and then call the lcm()
	 * method above. Calls the first lcm() method.
	 * 
	 * @param n
	 * @return factored least common multiple
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization lcm(long n) throws IllegalArgumentException {
		// Don't really know what this method wants from me
		if (n < 1) {
			throw new IllegalArgumentException("n < 1 in lcm(long n)");
		}
		return null;
	}

	/**
	 * Computes the least common multiple of the integers represented by pf1 and
	 * pf2.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the lcm of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization lcm(PrimeFactorization pf1, PrimeFactorization pf2) {
		// Dude honestly, I don't really know what this wants me to do.
		return null;
	}

	// ------------
	// List Methods
	// ------------

	/**
	 * Traverses the list to determine if p is a prime factor.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @return true if p is a prime factor of the number v represented by this
	 *         linked list false otherwise
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException {

		if (!isPrime(p)) {
			throw new IllegalArgumentException("p is not prime in containsPrimeFactor(int p)");
		} else {
			PrimeFactorizationIterator iterator = iterator();
			PrimeFactor pf1 = iterator.next();
			while (iterator.hasNext()) {
				if (pf1.prime == p) {
					return true;
				} else {
					pf1 = iterator.next();
				}
			}
		}
		return false;
	}

	// The next two methods ought to be private but are made public for testing
	// purpose. Keep
	// them public --- ok captain

	/**
	 * Adds a prime factor p of multiplicity m. Search for p in the linked list. If
	 * p is found at a node N, add m to N.multiplicity. Otherwise, create a new node
	 * to store p and m.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p prime
	 * @param m multiplicity
	 * @return true if m >= 1 false if m < 1
	 */
	public boolean add(int p, int m) {
		PrimeFactorizationIterator iterator = iterator();
		PrimeFactorizationIterator iteratorV2 = iterator();
		PrimeFactor pf1 = iteratorV2.next();
		//if size is 0 we can just chuck it in there
		if(size == 0) {
			PrimeFactor pf = new PrimeFactor(p, m);
			iterator.add(pf);
			if(m >= 1) {
				return true;
			}else {
				return false;
			}
		}
		while(true) {
			if(p < pf1.prime) {
				PrimeFactor pf2 = new PrimeFactor(p, m);
				pf1 = iteratorV2.previous();
				iteratorV2.add(pf2);
				if(m >= 1) {
					return true;
				}else {
					return false;
				}
			}else if(p == pf1.prime) {
				pf1.multiplicity += m;
				if(m >= 1) {
					return true;
				}else {
					return false;
				}
			}
			//Check if empty from now on, break if so
			if(!iteratorV2.hasNext()) {
				break;
			}else {
				pf1 = iteratorV2.next();
			}
		}
		PrimeFactor pf = new PrimeFactor(p,m);
		iteratorV2.add(pf);
		if(m >= 1) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Removes m from the multiplicity of a prime p on the linked list. It starts by
	 * searching for p. Returns false if p is not found, and true if p is found. In
	 * the latter case, let N be the node that stores p. If N.multiplicity > m,
	 * subtracts m from N.multiplicity. If N.multiplicity <= m, removes the node N.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @param m
	 * @return true when p is found. false when p is not found.
	 * @throws IllegalArgumentException if m < 1
	 */
	public boolean remove(int p, int m) throws IllegalArgumentException {
		// First lets see if m < 1
		if (m < 1) {
			throw new IllegalArgumentException("m < 1 in boolean remove(int p, int m)");
		}
		// Check if its prime first
		if (isPrime(p)) {
			PrimeFactorizationIterator iter = new PrimeFactorizationIterator();
			// While we have more we do stuff
			while (iter.hasNext()) {
				if (iter.cursor.pFactor.prime == p) {
					// subtract m from n.multiplicity as stated.
					if (iter.cursor.pFactor.multiplicity > m) {
						iter.cursor.pFactor.multiplicity -= m;
					}
					// remove node
					if (iter.cursor.pFactor.multiplicity <= m) {
						iter.remove();
					}
					iter.cursor = iter.cursor.next;
					// it was found so return true
					return true;
				}
			}
		}
		// it wasn't found so return false lul.
		return false;
	}

	/**
	 * 
	 * @return size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Writes out the list as a factorization in the form of a product. Represents
	 * exponentiation by a caret. For example, if the number is 5814, the returned
	 * string would be printed out as "2 * 3^2 * 17 * 19".
	 */
	@Override
	public String toString() {
		String str = "";
		PrimeFactorizationIterator p = iterator();
		for (int i = 0; i < this.size; i++) {
			if (p.hasNext()) {
				// If it has more than 1 multiplicity then we give the multiplicity in the str.
				if (p.next().multiplicity > 1) {
					str += p.next().prime + "^" + p.next().multiplicity;
				} else {
					// if it doesn't, we dont.
					str += p.next().prime;
				}
				if (p.hasNext()) {
					// if it has a next, we multiply it as given above.
					str += " * ";
				}
			}
		}
		return str;
	}

	// The next three methods are for testing, but you may use them as you like.

	/**
	 * @return true if this PrimeFactorization is representing a value that is too
	 *         large to be within long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if
	 *         valueOverflow()
	 */
	public long value() {
		return value;
	}

	// Given
	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}

	// Given
	@Override
	public PrimeFactorizationIterator iterator() {
		return new PrimeFactorizationIterator();
	}

	/**
	 * Doubly-linked node type for this class.
	 */
	private class Node {
		public PrimeFactor pFactor;
		public Node next;
		public Node previous;

		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {
			pFactor = null;
		}

		/**
		 * Precondition: p is a prime
		 * 
		 * @param p prime number
		 * @param m multiplicity
		 * @throws IllegalArgumentException if m < 1
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			if (m < 1 || (!isPrime(p))) {
				throw new IllegalArgumentException("m < 1 in Node(int p, int m)");
			}
			// My variable names reflect my mental state at the time of writing this.
			pFactor = new PrimeFactor(p, m);
		}

		/**
		 * Constructs a node over a provided PrimeFactor object.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			if (pf.multiplicity < 1) {
				throw new IllegalArgumentException("pf.multiplicity is < 1 in Node(PrimeFactor pf)");
			} else {
				new Node(pf.prime, pf.multiplicity);
			}
		}

		/**
		 * Printed out in the form: prime + "^" + multiplicity. For instance "2^3".
		 * Also, deal with the case pFactor == null in which a string "dummy" is
		 * returned instead.
		 */
		@Override
		public String toString() {
			if (pFactor == null) {
				// Cuz dummy!:), no cuz stated in javadoc above.
				return "dummy";
			} else {
				// not sure if I have to deal with multiplicity = 1 here or not. I don't think
				// so?
				return pFactor.prime + "^" + pFactor.multiplicity;
			}
		}
	}

	private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
		// Class invariants:
		// 1) logical cursor position is always between cursor.previous and cursor
		// 2) after a call to next(), cursor.previous refers to the node just returned
		// 3) after a call to previous() cursor refers to the node just returned
		// 4) index is always the logical index of node pointed to by cursor

		private Node cursor = head.next;
		private Node pending = null; // node pending for removal
		private int index = 0;

		// other instance variables ...

		/**
		 * Default constructor positions the cursor before the smallest prime factor.
		 */
		public PrimeFactorizationIterator() {
			cursor = head.next;
		}

		@Override
		public boolean hasNext() {
//			// Check if next is null or tail, if so return false, else true
//			if (cursor.next == null || cursor.next == tail) {
//				return false;
//			} else {
//				return true;
//			}
			return index < size;
		}

		@Override
		public boolean hasPrevious() {
//			// Same as hasNext() but the opposite kinda.
//			if (cursor.previous == null || cursor.previous == head) {
//				return false;
//			} else {
//				return true;
//			}
			return index > 0;
		}

		@Override
		public PrimeFactor next() {
			// If it doesn't have a next, it's null.
			if (!hasNext()) {
				return null;
			} else {
				PrimeFactor p = cursor.pFactor;
				//set pending node
				pending = cursor;
				cursor = cursor.next;
				index++;
				return p;
			}
		}

		@Override
		public PrimeFactor previous() {
			// If it doesn't have a previous, it's null
			if (!hasPrevious()) {
				return null;
			} else {
				pending = cursor;
				cursor = cursor.previous;
				index--;
				return cursor.pFactor;
			}
		}

		/**
		 * Removes the prime factor returned by next() or previous()
		 * 
		 * @throws IllegalStateException if pending == null
		 */
		@Override
		public void remove() throws IllegalStateException {
			// Throw for reason given
			if (pending == null) {
				throw new IllegalStateException("pending == null in remove()");
			} else {
				// else we remove pf returned by next or prevous.
				if (cursor == pending) {
					link(pending.previous, cursor.next);
				} else {
					link(pending.previous, cursor);

				}
				unlink(pending);
				pending = null;
			}
		}

		/**
		 * Adds a prime factor at the cursor position. The cursor is at a wrong position
		 * in either of the two situations below:
		 * 
		 * a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. b)
		 * pf.prime > cursor.pFactor.prime if cursor != tail.
		 * 
		 * Take into account the possibility that pf.prime == cursor.pFactor.prime.
		 * 
		 * Precondition: pf.prime is a prime.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException if the cursor is at a wrong position.
		 */
		@Override
		public void add(PrimeFactor pf) throws IllegalArgumentException {
			// Implement IllegalArgumentException - Going to do this here temporarily..?
			if (pf.prime < cursor.previous.pFactor.prime) {
				if (cursor.previous != head) {
					throw new IllegalArgumentException("pf.p < c.p.pf.p if c.p != h");
				}
			} else if (pf.prime > cursor.pFactor.prime) {
				if (cursor != tail) {
					throw new IllegalArgumentException("pf.p > c.pf.p if c != t");
				}
			}
			Node nodeToAdd = null;
			if (isPrime(pf.prime)) {
				if (cursor.previous != head) {
					if (pf.prime < cursor.previous.pFactor.prime) {
						nodeToAdd = new Node(pf);
						link(cursor.previous.previous, nodeToAdd);
					}
				}
				if (cursor != tail) {
					if (pf.prime > cursor.pFactor.prime) {
						nodeToAdd = new Node(pf);
						if (cursor != tail && cursor != head) {
							link(nodeToAdd, cursor.next);
							link(cursor, nodeToAdd);

						} else {
							link(cursor.previous, nodeToAdd);
						}
					}
					if (pf.prime == cursor.pFactor.prime) {
						cursor.pFactor.prime += pf.multiplicity;
					}
				} else {
					if (head.next == tail) {
						nodeToAdd = new Node(pf);
						link(nodeToAdd, cursor);
						link(head, nodeToAdd);
						index++;
					} else {
						nodeToAdd = new Node(pf);
						link(cursor.previous, nodeToAdd);
						link(nodeToAdd, cursor);
						index++;
					}
				}
			}
		}

		// Given
		@Override
		public int nextIndex() {
			return index;
		}

		// Given
		@Override
		public int previousIndex() {
			return index - 1;
		}

		// Given
		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...No
		//
	}

	// --------------
	// Helper methods
	// --------------

	/**
	 * Inserts toAdd into the list after current without updating size.
	 * 
	 * Precondition: current != null, toAdd != null
	 */
	private void link(Node current, Node toAdd) {
		// Check to see if current is null or toDdd is null, if they are do nothing.
		if (current == null || toAdd == null) {
			current.next = toAdd;
			toAdd.previous = current;
		}
	}

	/**
	 * Removes toRemove from the list without updating size.
	 */
	private void unlink(Node toRemove) {
		toRemove.next = null;
		toRemove.previous = null;
	}

	/**
	 * Remove all the nodes in the linked list except the two dummy nodes.
	 * 
	 * Made public for testing purpose. Ought to be private otherwise.
	 */
	public void clearList() {
		PrimeFactorizationIterator iter = new PrimeFactorizationIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
			size--;
		}
	}

	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the
	 * represented integer. Use Math.multiply(). If an exception is throw, assign
	 * OVERFLOW to the instance variable value. Otherwise, assign the multiplication
	 * result to the variable.
	 * 
	 */
	private void updateValue() {
		try {		
			PrimeFactorizationIterator iterator = iterator();	
			PrimeFactor pf = iterator.next();
			while(iterator.hasNext()){
				if(pf.multiplicity > 1){
					for(int i = 0; i < pf.multiplicity; i++){
						value+=Math.multiplyExact(pf.prime, pf.prime);
					}
					pf = iterator.next();	
				}
				else{
					value = Math.multiplyExact(pf.prime, value);
					pf = iterator.next();
				}
			}
		} catch (ArithmeticException e) {
			value = OVERFLOW;
		}
		
	}
}
