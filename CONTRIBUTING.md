Please note that while this repository is public, at the moment no
contributions from outside contributors are accepted. This project
is a TU Delft course project (TI2806) and the course rules disallow
this. As such, for now this documents merely acts as an internal set
of guidelines.

## Submitting contributions

- Make it clear in the issue tracker what you are working on.
- Keep the [sprint backlog][backlogs] up to date.
- Be descriptive in your pull request description: what is it for, why
  is it done this way, etc.
- Do ***not*** make cosmetic changes to unrelated files in the same pull
  request. This creates noise, making reviews harder to do.

### Tagging in the issue tracker

When submitting pull requests (commonly referred to as "PRs"), include
one of the following tags prepended to the title:

- `[WIP]` - Work In Progress: the PR will change, so while there is no
  immediate need for review, the submitter still might appreciate it.
- `[RFC]` - Request For Comment: the PR needs reviewing and/or comments.
- `[RDY]` - Ready: the PR has been reviewed by at least one other person and
  has no outstanding issues.

Please keep your tags up to date.

### Branching & history

- Do ***not*** work on your PR on the master branch, [use a feature branch
  instead][git-feature-branch]:
  <br/> `$ git checkout -b descriptiveFeatureName`
- [Rebase your feature branch onto][git-rebasing] (upstream) master before
  opening the PR:
```bash
$ git checkout master
$ git pull upstream/master
$ git checkout feature-branch
$ git rebase master
```
- Keep up to date with changes in (upstream) master so your PR is easy to
  merge.
- [Try to actively tidy your history][git-history-rewriting]: combine related
  commits with interactive rebasing, separate monolithic commits, etc. If your
  PR is still `[WIP]`, feel free to force-push to your feature branch to tidy
  your history. Please read [how to use rebase][git-rebasing], to see how to
  squash or otherwise edit your commits.

### For code pull requests

Before making a pull request, please rebase on upstream master (see above) and
provide a clean history (see above). Please also make sure to run `mvn test`,
to see if there are any outstanding issues (Checkstyle (see below), PMD, FindBugs, etc).

#### Testing

All PRs are expected to have (unit) tests to go with them. If no tests
are provided, PRs will not be merged unless there is a viable reason.
PRs will also not be merged if the Travis build fails.

#### Coding style

For new code, run Checkstyle to detect style errors. It's not perfect,
so some warnings may be false positives/negatives. Maven is set up to
fail on every style error. Should Checkstyle report too much false
positives/negatives, please edit Checkstyle's [configuration][checkstyle-config]. To have Checkstyle
ignore certain cases, see [this][so-checkstyle] stackoverflow question.

### Commit guidelines

The purpose of these guidelines is to *make reviews easier* and make
the history logs more valuable.

- Try to keep the first line under 72 characters.
- If necessary, include further description after a blank line.
    - Don't make the description too verbose by including obvious things, but
      don't spare clarifications for anything that may be not so obvious.
      Some commit messages are pages long, and that's fine if there's no
      better place for those comments to live.
    - **Recommended:** Prefix logically-related commits with a consistent
      identifier in each commit message. For already used identifiers, see the
      commit history for the respective file(s) you're editing.
      For example: `<ClassName>: fix all the issues`
- Use the [imperative voice][imperative]: "Fix bug" rather than "Fixed bug" or "Fixes bug."

### Reviewing pull requests

To assist code reviews, [Octopull][octopull] is used. This automatically does
a whole lot of static testing, speeding up the review process.

[Neovim][neovim] [provides a review checklist on its wiki][wiki-review-checklist],
that one can use besides Octopull.

You may find it easier to instead use an interactive program for code reviews,
such as [`tig`][tig].

<sup>Kindly taken and modified from [neovim][neovim].</sup>

[backlogs]: https://docs.google.com/spreadsheets/d/1r16xAJVS-ZjbkE4yLyhmW7in13BhqFE8-04LQggYHPg/edit?usp=sharing
[checkstyle-config]: https://github.com/DNAinator/dnainator/blob/master/checkstyle.xml
[so-checkstyle]: http://stackoverflow.com/questions/4023185/how-to-disable-a-particular-checkstyle-rule-for-a-particular-line-of-code
[git-bisect]: http://git-scm.com/book/tr/v2/Git-Tools-Debugging-with-Git
[git-feature-branch]: https://www.atlassian.com/git/tutorials/comparing-workflows
[git-history-filtering]: https://www.atlassian.com/git/tutorials/git-log/filtering-the-commit-history
[git-history-rewriting]: http://git-scm.com/book/en/v2/Git-Tools-Rewriting-History
[git-rebasing]: http://git-scm.com/book/en/v2/Git-Branching-Rebasing
[github-issues]: https://github.com/DNAinator/dnainator/issues
[imperative]: http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html
[style-guide]: http://neovim.io/develop/style-guide.xml
[octopull]: https://octopull.rmhartog.me/
[tig]: https://github.com/jonas/tig
[waffle]: https://waffle.io/neovim/neovim
[neovim]: http://neovim.io/
[wiki-review-checklist]: https://github.com/neovim/neovim/wiki/Code-review-checklist
