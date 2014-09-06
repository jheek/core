package org.wicketstuff.minis.resolver.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.wicket.Page;
import org.apache.wicket.core.util.lang.WicketObjects;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tag Support to generate a wicket url within a jsp. The tag library can be
 * defined within the <b>&lt;jsp-config&gt;</b> tag in <b>the web.xml</b>. Every
 * jar which is included in the <b>lib</b> folder of the webapp project will
 * automatically scanned if a taglib.tld is located in the <b>META-INF</b>
 * folder. If so the tag library is automatically added.
 * 
 * Example of web.xml definition:
 * 
 * <pre>
 * &lt;taglib&gt;
 * 	&lt;taglib-uri&gt;uri&lt;/taglib-uri&gt;
 * 	&lt;taglib-location&gt;/WEB-INF/jsp/mytaglib.tld&lt;taglib-location&gt;
 * &lt;/taglib&gt;
 * </pre>
 * 
 * Because the tld to this class is placed in the META-INF folder it is not required to add the taglib into the web.xml
 * 
 * Usage: To use the taglib and this tag you only have to define it in the jsp:
 * 
 * <pre>
 * &lt;%@ taglib prefix="wicket" uri="http://org.wicketstuff.minis.jsp/functions" %&gt;
 * 
 * Tag: url // Parameters: page(required), query(optional) // Example:
 * &lt;a href="&lt;wicket:url page="mypage.MyTestPage" query="param1=value1&amp;param2=value2"/&gt;"&gt;LINK&lt;/a&gt;
 * </pre>
 * 
 * @author Tobias Soloschenko
 */
public class WicketJSPURL extends TagSupport {

    private static final long serialVersionUID = 6146639184284158443L;

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(WicketJSPURL.class);

    private String page = null;

    private String query = null;

    /**
     * Applies the url of wicket to the tag
     */
    @Override
    public int doStartTag() throws JspException {
	try {
	    JspWriter out = pageContext.getOut();
	    PageParameters pageParameters = new PageParameters();
	    if (query != null) {
		RequestUtils.decodeParameters(query, pageParameters);
	    }
	    Class<Page> resolveClass = WicketObjects.resolveClass(page);
	    CharSequence urlFor = RequestCycle.get().urlFor(resolveClass,
		    pageParameters);
	    out.write(urlFor.toString());
	    out.flush();
	} catch (IOException e) {
	    LOGGER.error("Error while generating url for page " + page, e);
	    throw new JspException("Error while generating url for page ", e);
	}
	return SKIP_BODY;
    }

    public String getPage() {
	return page;
    }

    public void setPage(String page) {
	this.page = page;
    }

    public String getQuery() {
	return query;
    }

    public void setQuery(String query) {
	this.query = query;
    }
}
