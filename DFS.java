public static boolean DFS(ArrayList<Line2D> lines){
	int s = lines.size();


	double x1 = lines.get(0).getX1();
	double y1 = lines.get(0).getY1();

	double x2 = lines.get(0).getX2();
	double y2 = lines.get(0).getY2();

	//one edge has been covered

	int count = 1;

	int c = 0;

	//loop until we reach the start node, or we have covered all edges
	while (!(X2 == X1 && Y2 == Y1) && cout < s){
	
		for(int i = 0; i <lines.size(); i++ ){
			//find a line who's start point is the same as this one's end point
			if(lines.get(i).getX1() == X2 && lines.get(i).getY1() == Y2){
				//set c to i, get it's X2 and Y2
				c = i;
				X2 = lines.get(c).getX2();
				Y2 = lines.get(c).getY2();
				//increment the count of edges
				count ++;
				break;
			}
			//if it's actually marked using it's end node, check for that
			//be sure that it's not actually the same line by checking if i=c
			else if(i!=c && lines.get(i).getX2() == X2 && lines.get(i).getY2() == Y2){
				c = i;
				X2 = lines.get(c).getX1();
				Y2 = lines.get(c).getY1();
				//increment the count of edges
				count ++;
				break;
			}

		}}
		//return whether all edges were visited
		return (count == s);
}


public static boolean allEdges(ArrayList<Line2D> lines){

	//get the size of the list
	int s = lines.size();
	//make a copy of lines
	ArrayList<Line2D> copLines = new ArrayList<Line2D>(lines);

	//get the coordinates for the line
	int x1 = copLines.get(0).getX1;
	int y1 = copLines.get(0).getY1;
	int x2 = copLines.get(0).getX2;
	int y2 = copLines.get(0).getY2;

	//remove the line from copLines
	copLines.remove(c);
	int iX1 = 0;
	int iY1 = 0;
	int iX2 = 0;
	int iY2 = 0;

	//find a line, still in copLines, with a matching endpoint
	while(!(copLines.isEmpty()){
		boolean match = false;
		for(int i = 0; i < copLines.size(); i++){
			iX1 = copLines.get(i).getX1;
			iY1 = copLines.get(i).getY1;
			iX2 = copLines.get(i).getX2;
			iY2 = copLines.get(i).getY2;
			if((iX1==x1 && iY1==y1) || (iX2==x1 && iY2==y1) || (iX1==x2 && iY1==y2) || (iX2==x2 && iY2==y2)){
				//remove the line from copLines
				copLines.remove(i);
				//set the x and y's to the new values
				x1 = iX1;
				y1 = iY1;
				x2 = iX2;
				y2 = iY2;
				//a match was found
				match = true;
				//break from the for loop
				break;
			}
	}//if there was not a match, break from the while loop, otherwise continue
	if (!match){
		break;
	}
}
//if the lines are empty, then all lines were connected and the loop is one piece, return true
return copLines.isEmpty();


}