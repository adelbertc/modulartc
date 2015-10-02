package modulartc

// Section 2.1
trait Eq[A] {
  def eqv(x: A, y: A): Boolean
}

object Eq {
  def prod[A, B](implicit A: Eq[A], B: Eq[B]): Eq[(A, B)] =
    new Eq[(A, B)] {
      def eqv(x: (A, B), y: (A, B)): Boolean =
        A.eqv(x._1, y._1) && B.eqv(x._2, y._2)
    }

  val eqInt: Eq[Int] =
    new Eq[Int] { def eqv(x: Int, y: Int): Boolean = x == y }
}

// Section 2.3
trait Lt[A] {
  def lt(x: A, y: A): Boolean
}

object Lt {
  def prod[A, B](implicit A: Ord[A], B: Lt[B]): Lt[(A, B)] =
    new Lt[(A, B)] {
      def lt(x: (A, B), y: (A, B)): Boolean =
        (A.L.lt(x._1, y._1)) || (A.E.eqv(x._1, y._1) && B.lt(x._2, y._2))
    }

  val ltInt: Lt[Int] =
    new Lt[Int] { def lt(x: Int, y: Int): Boolean = x < y }
}

trait Ord[A] {
  val E: Eq[A]
  val L: Lt[A]
  final def compare(x: A, y: A): Int =
    if (L.lt(x, y)) -1
    else if (E.eqv(x, y)) 0
    else 1
}

object Ord {
  implicit def synthesize[A](implicit E0: Eq[A], L0: Lt[A]): Ord[A] =
    new Ord[A] {
      val E = E0
      val L = L0
    }
}

// Section 2.5
trait Collects[A] {
  type Elem
  val empty: A
  def cons(h: Elem, t: A): A
  def member(e: Elem, a: A): Boolean
  def toList(a: A): List[Elem]
}

object Collects {
  def collectsList[A](implicit A: Eq[A]): Collects[List[A]] =
    new Collects[List[A]] {
      type Elem = A
      val empty: List[A] = List.empty[A]
      def cons(h: A, t: List[A]): List[A] = h :: t
      def member(e: A, a: List[A]): Boolean = a.contains(e)
      def toList(a: List[A]): List[A] = a
    }
}

object ModularTc extends App {
  // Section 2.2
  imply(Eq.eqInt, Eq.prod) {
    val instance: Eq[(Int, Int)] = implicitly[Eq[(Int, Int)]]
  }

  // Does not compile
  // val instance: Eq[(Int, Int)] = implicitly[Eq[(Int, Int)]]

  // Section 2.3
  imply(Eq.eqInt, Lt.ltInt) {
    val instance: Ord[Int] = implicitly[Ord[Int]]
  }

  // Section 2.5
  imply(Eq.eqInt, Collects.collectsList) {
    val instance: Collects[List[Int]] = implicitly[Collects[List[Int]]]
  }

  // Does not compile
  // imply(Collects.collectsList) {
  //   val instance: Collects[List[Int]] = implicitly[Collects[List[Int]]]
  // }
}
