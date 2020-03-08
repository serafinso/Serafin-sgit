package sgit.objectManipulation

import sgit.objects.Commit

object commitManipulation {
  /** PF method
   *
   * @param commits commit list
   * @return make tuple with the commits list . Transforms the commit list and create commit pairs.
   *         These tuples are made up of all commits with his previous commit.
   */
  def getCommitInTuple (commits : List[Commit]) : List[(Commit,Commit)] = {
    if(commits.isEmpty || commits.tail.isEmpty) List.empty
    else (commits.head, commits.tail.head)::getCommitInTuple(commits.tail)
  }
}
