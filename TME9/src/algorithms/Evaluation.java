package algorithms;
import java.util.ArrayList;
import java.awt.Point;

public class Evaluation {
  private boolean isMember(ArrayList<Point> points, Point p){
    for (Point point:points) if (point.equals(p)) return true; return false;
  }
  public boolean isValid(ArrayList<Point> origPoints, ArrayList<Point> fvs, int edgeThreshold){
    ArrayList<Point> vertices = new ArrayList<Point>();
    for (Point p:origPoints) if (!isMember(fvs,p)) vertices.add((Point)p.clone());

    //Looking for loops in subgraph induced by origPoint \setminus fvs
    while (!vertices.isEmpty()){
      ArrayList<Point> green = new ArrayList<Point>();
      green.add((Point)vertices.get(0).clone());
      ArrayList<Point> black = new ArrayList<Point>();

      while (!green.isEmpty()){
        for (Point p:neighbor(green.get(0),vertices,edgeThreshold)){
          if (green.get(0).equals(p)) continue;
          if (isMember(black,p)) return false;
          if (isMember(green,p)) return false;
          green.add((Point)p.clone());
        }
        black.add((Point)green.get(0).clone());
        vertices.remove(green.get(0));
        green.remove(0);
      }
    }

    return true;
  }
  private ArrayList<Point> neighbor(Point p, ArrayList<Point> vertices, int edgeThreshold){
    ArrayList<Point> result = new ArrayList<Point>();

    for (Point point:vertices) if (point.distance(p)<edgeThreshold && !point.equals(p)) result.add((Point)point.clone());

    return result;
  }
}
