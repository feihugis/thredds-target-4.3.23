package thredds.catalog;

import java.io.IOException;
import java.util.List;

import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Created by Fei Hu on 4/15/16.
 */
public class TestGrib1 {

  public static void main(String[] args) throws IOException, InvalidRangeException {
    NetcdfFile nc = NetcdfFile.open("/Users/feihu/Documents/Data/CFSR/pgbh06.gdas.A_PCP.SFC.grb2");
    List<Variable> variableList = nc.getVariables();
    for (Variable var : variableList) {
      var = variableList.get(4);
      System.out.println(var.getDescription());
      var.read(new int[]{0,0,0}, new int[]{1,361,720});
    }
  }

}
