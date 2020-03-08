package sgit.objects

/** Ref object
 *
 * @param commitKey The commit key related to the ref
 * @param name the ref name
 */
case class Ref(commitKey : String, name : String) {}
