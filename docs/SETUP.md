# MokuLib Setup Notes

## Git

Initialize a Git repository:

- `git init`
- `git branch -M main`

Configure username and email for Git:

- `git config --local user.name "廖浩龙"`
- `git config --local user.email "aliaohaolong@gmail.com"`

Please replace the above values with your own.

Link the local repository to the remote repository:

- `git remote add origin git@github.com:mokulib/mokulib-server.git`

Do first commit and push to the remote repository:

- `git add README.md`
- `git commit -m "docs: add README.md"`
- `git push -u origin main`