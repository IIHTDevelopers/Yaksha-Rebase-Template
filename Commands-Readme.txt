* to execute and run test cases

  mvn clean install exec:java -Dexec.mainClass="mainapp.MyApp" -DskipTests=true

git config --global user.email ""
git config --global user.name ""
==============================================================================
echo "First commit on main" > file.txt
git add file.txt
git commit -m "Initial commit on main"

git checkout -b feature-branch
echo "Feature work 1" > feature.txt
git add feature.txt
git commit -m "Add feature work 1"

echo "Feature work 2" >> feature.txt
git add feature.txt
git commit -m "Add feature work 2"

git checkout main
echo "Changes on main" >> file.txt
git add file.txt
git commit -m "Changes made on main"

git checkout main

git add .
git commit -m "adding all files"

git rebase feature-branch
