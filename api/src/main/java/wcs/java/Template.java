package wcs.java;

import static wcs.java.util.Util.attrArray;
import static wcs.java.util.Util.attrBlob;
import static wcs.java.util.Util.attrString;
import static wcs.java.util.Util.attrStruct;
import static wcs.java.util.Util.attrStructKV;

import java.util.HashMap;
import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public class Template extends AssetSetup {

	public final static char UNSPECIFIED = '\0';
	public final static char INTERNAL = 'b';
	public final static char STREAMED = 'r';
	public final static char EXTERNAL = 'x';
	public final static char LAYOUT = 'l';

	private String rootelement;
	private String fileelement;
	private String folderelement;
	private String clazz;
	private String cscache;
	private String sscache;
	private char ttype;
	private String forSubtype;

	public Template(long id, String subtype, String name, char ttype,
			Class<?> elementClass) {
		this(id, subtype, name, ttype, null, elementClass);
	}

	/**
	 * Create a template with given subtype, name, and top element, applying to
	 * given subtypes.
	 * 
	 * Description defaults to the name, cache defaults to false/false
	 * 
	 * @param subtype
	 * @param name
	 * @param description
	 * @param element
	 */
	public Template(long id, String subtype, String name, char ttype, String forSubtype,
			Class<?> elementClass) {
		super(id, "Template", subtype, name);
		this.clazz = elementClass.getCanonicalName();
		if (subtype == null || subtype.trim().length() == 0) {
			subtype = "";
			rootelement = "/" + name;
			folderelement = "Typeless";
		} else {
			subtype = subtype.trim();
			rootelement = subtype + "/" + name;
			folderelement = subtype;
		}
		fileelement = name + "_" + clazz + ".jsp";

		this.ttype = ttype;
		if (forSubtype == null || forSubtype.trim().length() == 0)
			this.forSubtype = "*";
		else
			this.forSubtype = forSubtype;
		cache("false", "false");
	}

	/**
	 * Setup a template with a chained setup
	 * 
	 * @param subtype
	 * @param name
	 * @param ttype
	 * @param elementClass
	 * @param nextSetup
	 */
	public Template(long id, String subtype, String name, char ttype,
			Class<?> elementClass, AssetSetup nextSetup) {
		this(id, subtype, name, ttype, elementClass);
		setNextSetup(nextSetup);
	}

	/**
	 * Fluent cache setter
	 * 
	 * @param cscache
	 * @param sscache
	 * @return
	 */

	public Template cache(String cscache, String sscache) {
		this.cscache = cscache;
		this.sscache = sscache;
		return this;
	}

	public String getElement() {
		return rootelement;
	}

	public String getCscache() {
		return cscache;
	}

	public String getSscache() {
		return sscache;
	}

	@Override
	public List<String> getAttributes() {
		return Util.listString("name", "description", "category",
				"rootelement", "element", "ttype", "pagecriteria", "acl",
				"applicablesubtypes", "Thumbnail");
	}

	private String template(String clazz) {
		return Util.getResource("/Streamer.jsp").replaceAll("%CLASS%", clazz);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setData(MutableAssetData data) {

		// log.info(Util.dump(data));
		// String rootelement = getSubtype() + "/" + getName();

		final String body = template(clazz);

		final AttributeData blob = attrBlob("url", folderelement, fileelement,
				body);

		HashMap mapElement = new HashMap<String, Object>();

		mapElement.put("elementname", attrString("elementname", rootelement));
		mapElement.put("description", attrString("description", rootelement));
		mapElement.put("resdetails1",
				attrString("resdetails1", "tid=" + data.getAssetId().getId()));
		mapElement.put(
				"resdetails2",
				attrString("resdetails2",
						"agilesites=" + System.currentTimeMillis()));
		mapElement.put("csstatus", attrString("csstatus", "live"));
		mapElement.put("cscacheinfo", attrString("cscacheinfo", cscache));
		mapElement.put("sscacheinfo", attrString("sscacheinfo", sscache));
		mapElement.put("url", blob);

		HashMap mapSiteEntry = new HashMap<String, Object>();
		mapSiteEntry.put("pagename", attrString("pagename", rootelement));
		mapSiteEntry.put(
				"defaultarguments", //
				attrArray(
						"defaultarguments", //
						attrStructKV("site", getSite()),
						attrStructKV("rendermode", "live")));
		mapElement.put(
				"siteentry",
				attrArray("siteentry",
						attrStruct("Structure siteentry", mapSiteEntry)));

		data.getAttributeData("category").setData("banr");

		data.getAttributeData("rootelement").setData(rootelement);

		data.getAttributeData("element").setData(
				Util.list(attrStruct("Structure Element", mapElement)));

		// default page criteria
		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", /* "p", */"rendermode",
						"site", /* "sitepfx", */"ft_ss"));

		data.getAttributeData("acl").setDataAsList(Util.listString(""));

		data.getAttributeData("ttype").setData(
				ttype == UNSPECIFIED ? null : "" + ttype);

		data.getAttributeData("applicablesubtypes").setData(forSubtype);
	}

	/**
	 * Fluent description setter
	 * 
	 * @param description
	 * @return
	 */
	public AssetSetup description(String description) {
		setDescription(description);
		return this;
	}
}
