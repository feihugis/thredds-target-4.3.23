/*
 * Copyright (c) 1998 - 2009. University Corporation for Atmospheric Research/Unidata
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ucar.nc2.ft.point.writer;

import ucar.nc2.constants.CDM;
import ucar.nc2.constants.CF;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.units.DateUnit;
import ucar.nc2.*;
import ucar.nc2.ft.*;
import ucar.unidata.geoloc.LatLonRect;
import ucar.unidata.geoloc.LatLonPointImpl;
import ucar.unidata.geoloc.Station;
import ucar.ma2.*;

import java.util.*;
import java.io.IOException;

/**
 * Write a CF "Discrete Sample" station file.
 * Example H.7. Timeseries of station data in the indexed ragged array representation.
 *
 * <p/>
 * <pre>
 *   writeHeader()
 *   iterate { writeRecord() }
 *   finish()
 * </pre>
 *
 * @see "http://cf-pcmdi.llnl.gov/documents/cf-conventions/1.6/cf-conventions.html#idp8340320"
 * @author caron
 * @since Aug 19, 2009
 */
public class WriterCFStationCollection  extends CFPointWriter {
  private static final String stationDimName = "station";
  private static final String idName = "station_id";
  private static final String descName = "station_description";
  private static final String wmoName = "wmo_id";
  private static final String stationIndexName = "stationIndex";
  private static final boolean debug = false;

  //////////////////////////////////////////////////////////
  private int name_strlen = 1, desc_strlen = 1, wmo_strlen = 1;
  private Variable lat, lon, alt, time, id, wmoId, desc, stationIndex, record;

  private List<Dimension> stationDims = new ArrayList<Dimension>(1);

  private boolean useAlt = false;
  private boolean useWmoId = false;

  public WriterCFStationCollection(String fileOut, String title) throws IOException {
    this(null, fileOut, Arrays.asList(new Attribute[]{new Attribute(CDM.TITLE, title)}));
  }
  
  public WriterCFStationCollection(NetcdfFileWriter.Version version, String fileOut, List<Attribute> atts) throws IOException {
    super(fileOut, atts, version);
    writer.addGroupAttribute(null, new Attribute(CF.FEATURE_TYPE, CF.FeatureType.timeSeries.name()));
  }

  public void writeHeader(List<ucar.unidata.geoloc.Station> stns, List<VariableSimpleIF> vars, DateUnit timeUnit, String altUnits) throws IOException {
    this.altUnits = altUnits;

    createStations(stns);
    createObsVariables(timeUnit);
    createDataVariables(vars);

    writer.create(); // done with define mode
    record = writer.addRecordStructure();

    writeStationData(stns); // write out the station info
  }

  private void createStations(List<ucar.unidata.geoloc.Station> stnList) throws IOException {
    int nstns = stnList.size();

    // see if there's altitude, wmoId for any stations
    for (Station stn : stnList) {
      if (!Double.isNaN(stn.getAltitude()))
        useAlt = true;
      if ((stn.getWmoId() != null) && (stn.getWmoId().trim().length() > 0))
        useWmoId = true;
    }

    /* if (useAlt)
      ncfile.addGlobalAttribute("altitude_coordinate", altName); */

    // find string lengths
    for (Station station : stnList) {
      name_strlen = Math.max(name_strlen, station.getName().length());
      desc_strlen = Math.max(desc_strlen, station.getDescription().length());
      if (useWmoId) wmo_strlen = Math.max(wmo_strlen, station.getWmoId().length());
    }

    llbb = getBoundingBox(stnList); // gets written in super.finish();

    // add the dimensions
    writer.addUnlimitedDimension(recordDimName);
    Dimension stationDim = writer.addDimension(null, stationDimName, nstns);
    stationDims.add(stationDim);

    // add the station Variables using the station dimension
    lat = writer.addVariable(null, latName, DataType.DOUBLE, stationDimName);
    writer.addVariableAttribute(lat, new Attribute(CDM.UNITS, CDM.LAT_UNITS));
    writer.addVariableAttribute(lat, new Attribute(CDM.LONG_NAME, "station latitude"));

    lon = writer.addVariable(null, lonName, DataType.DOUBLE, stationDimName);
    writer.addVariableAttribute(lon, new Attribute(CDM.UNITS, CDM.LON_UNITS));
    writer.addVariableAttribute(lon, new Attribute(CDM.LONG_NAME, "station longitude"));

    if (useAlt) {
      alt = writer.addVariable(null, altName, DataType.DOUBLE, stationDimName);
      writer.addVariableAttribute(alt, new Attribute(CDM.UNITS, "meters"));
      writer.addVariableAttribute(alt, new Attribute(CF.POSITIVE, CF.POSITIVE_UP));
      writer.addVariableAttribute(alt, new Attribute(CDM.LONG_NAME, "station altitude"));
      writer.addVariableAttribute(alt, new Attribute(CF.STANDARD_NAME, CF.SURFACE_ALTITUDE));
    }

    id = writer.addStringVariable(null, idName, stationDims, name_strlen);
    writer.addVariableAttribute(id, new Attribute(CDM.LONG_NAME, "station identifier"));
    writer.addVariableAttribute(id, new Attribute(CF.CF_ROLE, CF.TIMESERIES_ID));  // station_id:cf_role = "timeseries_id";
    
    desc = writer.addStringVariable(null, descName, stationDims, desc_strlen);
    writer.addVariableAttribute(desc, new Attribute(CDM.LONG_NAME, "station description"));
    writer.addVariableAttribute(desc, new Attribute(CF.STANDARD_NAME, CF.PLATFORM_NAME));

    if (useWmoId) {
      wmoId = writer.addStringVariable(null, wmoName, stationDims, wmo_strlen);
      writer.addVariableAttribute(wmoId, new Attribute(CDM.LONG_NAME, "station WMO id"));
      writer.addVariableAttribute(wmoId, new Attribute(CF.STANDARD_NAME, CF.PLATFORM_ID));
    }
  }

  private void createObsVariables(DateUnit timeUnit) throws IOException {
    // time variable
	  
    time = writer.addVariable(null, timeName, DataType.DOUBLE, recordDimName);
    writer.addVariableAttribute(time, new Attribute(CDM.UNITS, timeUnit.getUnitsString()));
    writer.addVariableAttribute(time, new Attribute(CDM.LONG_NAME, "time of measurement"));

    stationIndex = writer.addVariable(null, stationIndexName, DataType.INT, recordDimName);
    writer.addVariableAttribute(stationIndex, new Attribute(CDM.LONG_NAME, "station index for this observation record"));
    writer.addVariableAttribute(stationIndex, new Attribute(CF.INSTANCE_DIMENSION, stationDimName));
  }

  private void createDataVariables(List<VariableSimpleIF> dataVars) throws IOException {
    String coordNames = latName + " " + lonName + " " + altName + " " + timeName;
    if(!useAlt){
    	coordNames = latName + " " + lonName + " " + timeName;
    }

    // find all dimensions needed by the data variables
    for (VariableSimpleIF var : dataVars) {
      List<Dimension> dims = var.getDimensions();
      dimSet.addAll(dims);
    }

    // add them
    for (Dimension d : dimSet) {
      if (!d.isUnlimited())
        writer.addDimension(null, d.getShortName(), d.getLength(), d.isShared(), false, d.isVariableLength());
    }
    
    // find all variables already in use 
    List<VariableSimpleIF> useDataVars = new ArrayList<VariableSimpleIF>(dataVars.size());
    for (VariableSimpleIF var : dataVars) {
      
      if (writer.findVariable(var.getShortName()) == null) useDataVars.add(var);
    }

    // add the data variables all using the record dimension
    for (VariableSimpleIF oldVar : useDataVars) {
      List<Dimension> dims = oldVar.getDimensions();
      StringBuilder dimNames = new StringBuilder(recordDimName);
      for (Dimension d : dims) {
        if (!d.isUnlimited())
          dimNames.append(" ").append(d.getShortName());
      }
      Variable newVar = writer.addVariable(null, oldVar.getShortName(), oldVar.getDataType(), dimNames.toString());

      List<Attribute> atts = oldVar.getAttributes();
      for (Attribute att : atts) {
        newVar.addAttribute(att);
      }
      newVar.addAttribute(new Attribute(CF.COORDINATES, coordNames));
    }
  }

  private HashMap<String, Integer> stationMap;

  private void writeStationData(List<ucar.unidata.geoloc.Station> stnList) throws IOException {
    int nstns = stnList.size();
    stationMap = new HashMap<String, Integer>(2 * nstns);
    if (debug) System.out.println("stationMap created");

    // now write the station data
    ArrayDouble.D1 latArray = new ArrayDouble.D1(nstns);
    ArrayDouble.D1 lonArray = new ArrayDouble.D1(nstns);
    ArrayDouble.D1 altArray = new ArrayDouble.D1(nstns);
    ArrayObject.D1 idArray = new ArrayObject.D1(String.class, nstns);
    ArrayObject.D1 descArray = new ArrayObject.D1(String.class, nstns);
    ArrayObject.D1 wmoArray = new ArrayObject.D1(String.class, nstns);

    for (int i = 0; i < stnList.size(); i++) {
      ucar.unidata.geoloc.Station stn = stnList.get(i);
      stationMap.put(stn.getName(), i);

      latArray.set(i, stn.getLatitude());
      lonArray.set(i, stn.getLongitude());
      if (useAlt) altArray.set(i, stn.getAltitude());

      idArray.set(i, stn.getName());
      descArray.set(i, stn.getDescription());
      if (useWmoId) wmoArray.set(i, stn.getWmoId());
    }

    try {
      writer.write(lat, latArray);
      writer.write(lon, lonArray);
      if (useAlt) writer.write(alt, altArray);
      writer.writeStringData(id, idArray);
      writer.writeStringData(desc, descArray);
      if (useWmoId) writer.writeStringData(wmoId, wmoArray);

    } catch (InvalidRangeException e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }
  }

  private int recno = 0;
  private ArrayDouble.D1 timeArray = new ArrayDouble.D1(1);
  //private ArrayInt.D1 prevArray = new ArrayInt.D1(1);
  private ArrayInt.D1 parentArray = new ArrayInt.D1(1);
  private int[] origin = new int[1];

  public void writeRecord(Station s, PointFeature sobs, StructureData sdata) throws IOException {
    writeRecord(s.getName(), sobs.getObservationTime(), sobs.getObservationTimeAsCalendarDate(), sdata);
  }

  public void writeRecord(String stnName, double timeCoordValue, CalendarDate obsDate, StructureData sdata) throws IOException {
    trackBB(null, obsDate);

    Integer parentIndex = stationMap.get(stnName);
    if (parentIndex == null)
      throw new RuntimeException("Cant find station " + stnName);

    // needs to be wrapped as an ArrayStructure, even though we are only writing one at a time.
    ArrayStructureW sArray = new ArrayStructureW(sdata.getStructureMembers(), new int[]{1});
    sArray.setStructureData(sdata, 0);

    timeArray.set(0, timeCoordValue);
    parentArray.set(0, parentIndex);

    // write the recno record
    origin[0] = recno;
    try {
      writer.write(record, origin, sArray);
      writer.write(time, origin, timeArray);
      writer.write(stationIndex, origin, parentArray);

    } catch (InvalidRangeException e) {
      e.printStackTrace();
      throw new IllegalStateException(e);
    }

    recno++;
  }

  private LatLonRect getBoundingBox(List stnList) {
    ucar.unidata.geoloc.Station s = (ucar.unidata.geoloc.Station) stnList.get(0);
    LatLonPointImpl llpt = new LatLonPointImpl();
    llpt.set(s.getLatitude(), s.getLongitude());
    LatLonRect rect = new LatLonRect(llpt, .001, .001);

    for (int i = 1; i < stnList.size(); i++) {
      s = (ucar.unidata.geoloc.Station) stnList.get(i);
      llpt.set(s.getLatitude(), s.getLongitude());
      rect.extend(llpt);
    }

    return rect;
  }

  ////////////////////////////

    /* public static void rewrite(String fileIn, String fileOut) throws IOException {
    long start = System.currentTimeMillis();

    // do it in memory for speed
    NetcdfFile ncfile = NetcdfFile.openInMemory(fileIn);
    NetcdfDataset ncd = new NetcdfDataset( ncfile);

    StringBuilder errlog = new StringBuilder();
    StationObsDataset sobs = (StationObsDataset) TypedDatasetFactory.open(FeatureType.STATION, ncd, null, errlog);

    List<ucar.unidata.geoloc.Station> stns = sobs.getStations();
    List<VariableSimpleIF> vars = sobs.getDataVariables();

    WriterStationObsDataset writer = new WriterStationObsDataset(fileOut, "rewrite "+fileIn);
    File f = new File( fileIn);
    writer.setLength(f.length());
    writer.writeHeader(stns, vars);

    for (ucar.unidata.geoloc.Station s : stns) {
      DataIterator iter = sobs.getDataIterator(s);
      while (iter.hasNext()) {
        StationObsDatatype sobsData = (StationObsDatatype) iter.nextData();
        StructureData data = sobsData.getData();
        writer.writeRecord(sobsData, data);
      }
    }

    writer.finish();

    long took = System.currentTimeMillis() - start;
    System.out.println("Rewrite " + fileIn+" to "+fileOut+ " took = " + took);
  }

  public static void main2(String args[]) throws IOException {
    long start = System.currentTimeMillis();
    String toLocation = "C:/temp2/";
    String fromLocation = "C:/data/metars/";

    if (args.length > 1) {
      fromLocation = args[0];
      toLocation = args[1];
    }
    System.out.println("Rewrite .nc files from "+fromLocation+" to "+toLocation);

    File dir = new File(fromLocation);
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.getName().endsWith(".nc"))
        rewrite(file.getAbsolutePath(), toLocation + file.getName());
    }

    long took = System.currentTimeMillis() - start;
    System.out.println("That took = " + took);

  }   */

}