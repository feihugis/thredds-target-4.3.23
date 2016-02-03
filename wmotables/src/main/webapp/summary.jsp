<%@ include file="/WEB-INF/views/include/tablibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
 <html>
  <head>
   <title><spring:message code="global.title"/>: Summary Report</title>
<%@ include file="/WEB-INF/views/include/resources.jsp" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<h3>Summary report on the suitability of GRIB/BUFR for archiving data</h3>
<p><em>John Caron, Feb 2011</em></p>
<p> A major weakness of the BUFR and GRIB formats are their dependence on   external tables. For GRIB, one must have the correct tables in order to   understand the meaning of the data. For BUFR, the correct tables are   needed both to understand the meaning of the data and also to parse the data.   An important design flaw in these formats is that there is no foolproof   way to know when you have the correct tables, that is, for a reader to   know for certain which tables the writer used.<br />
    <br />
  Compounding this design flaw are implementation practices which make this problem occur more often than might be expected.</p>
<ol>
  <li>The WMO publishes the standard tables in Word and PDF format, neither   of which are machine readable. Software that write GRIB and BUFR   therefore have their own versions of the standard tables, in various   formats, with various lineages. Of the 5 or 6 packages I examined, no   two agreed exactly. Some of the tables are subsets of the WMO versions,   probably because the software maintainers are only interested in the   entries used by their organization. The tables often appear to be   maintained by hand.</li>
  <li> There are a few typographical errors in the published WMO standard   tables. With enough effort, humans can track down the correct values.   However it is  very difficult to know whether any   given BUFR/GRIB message used the correct or incorrect value.</li>
  <li> The process of adding new entries to WMO standard tables can lead to   incorrect entries. Member organizations propose new entries, which are   assigned preliminary ids. Messages using these preliminary ids are   sometimes generated before the ids are finalized. The ids sometimes are   changed before the version becomes final. </li>
  <li> The table version information is often wrong. In principle with   correct table version information and correct tables, the mismatch   problem does not occur. In practice, the table version numbers are often   wrong in the message. This may be due to a casual attitude towards   encoding the correct version, or it may be due to difficulty in identifying the correct version,   especially with the complications of item #3.</li>
  <li> Local tables are often used, presumably because the standard tables   are not rich enough. All the problems of tracking tables and versions   are thus compounded. Local tables sometimes use ids that are reserved   for the WMO. This essentially makes these messages unreadable by anyone   other than those who generate the messages.</li>
</ol>
<p> In summary, these problems make BUFR/GRIB not suitable as long-term   storage formats, in my opinion. In order to make them suitable, 1) there   must be a foolproof way for reading software to know what tables the   writing software used, and 2) there must be an authoritative registry of   tables.<br />
    <br />
  A general solution that I would recommend is the following:</p>
<ol>
  <li>The WMO or its delegate establishes a web service for registering   tables. Authorized users can submit tables to the service, which are   stored permanently. The service returns a unique hashcode, (eg the   16-byte MD5 checksum) for the table to the user. </li>
  <li> The hashcode for the table that the writer uses is encoded into the   BUFR/GRIB message that is written. This may require a new version of   the encoding.</li>
  <li>Anyone can submit a hashcode to the web service and get the table associated with it.</li>
</ol>
<p>Difficulties with tables are further compounded by mistakes in encoding and decoding software. Therefore I would also recommend that:</p>
<ul>
  <li>The WMO or its delegate creates reference software that can be used to   validate that a BUFR/GRIB message is well-formed,&nbsp; and parses the   message and applies the canonical tables, returning a result that can be   used to validate other software. This could also be a web service, in   addition to being an open-source library. The reference software can be   written in any language and need not be high performance. </li>
</ul>
   
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
  </body>
 </html>
