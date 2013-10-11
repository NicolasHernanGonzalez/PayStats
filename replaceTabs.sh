PROYECT_ROOT_FOLDER=/c/Users/n.gonzalez/workspace/PayStadistics

cd $PROYECT_ROOT_FOLDER
for i in `find . -name *.java`
do
	sed -i 's/\t/    /g' $i
done
	