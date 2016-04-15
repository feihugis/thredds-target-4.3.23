package thredds.catalog;

import java.io.IOException;
import java.util.List;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Created by Fei Hu on 4/15/16.
 */
public class TestGrib1 {

  public static void main(String[] args) throws IOException {
    NetcdfFile nc = NetcdfFile.open("/Users/feihu/Documents/Data/era-interim/ei.mdfa.fc12hr.sfc.regn128sc.1980010100");
    List<Variable> variableList = nc.getVariables();
    for (Variable var : variableList) {
      System.out.println(var.getDescription());
    }
  }

}
