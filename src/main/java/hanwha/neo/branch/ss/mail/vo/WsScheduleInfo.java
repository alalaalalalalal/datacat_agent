/**
 * WsScheduleInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package hanwha.neo.branch.ss.mail.vo;

public class WsScheduleInfo  implements java.io.Serializable {
    private int alarmTime;

    private boolean allDay;

    private java.lang.String endDate;

    private boolean htmlContent;

    private boolean important;

    private java.lang.String location;

    private boolean mhtContent;

    private java.lang.String senderEmail;

    private java.lang.String startDate;

    private java.lang.String subject;

    public WsScheduleInfo() {
    }

    public WsScheduleInfo(
           int alarmTime,
           boolean allDay,
           java.lang.String endDate,
           boolean htmlContent,
           boolean important,
           java.lang.String location,
           boolean mhtContent,
           java.lang.String senderEmail,
           java.lang.String startDate,
           java.lang.String subject) {
           this.alarmTime = alarmTime;
           this.allDay = allDay;
           this.endDate = endDate;
           this.htmlContent = htmlContent;
           this.important = important;
           this.location = location;
           this.mhtContent = mhtContent;
           this.senderEmail = senderEmail;
           this.startDate = startDate;
           this.subject = subject;
    }


    /**
     * Gets the alarmTime value for this WsScheduleInfo.
     * 
     * @return alarmTime
     */
    public int getAlarmTime() {
        return alarmTime;
    }


    /**
     * Sets the alarmTime value for this WsScheduleInfo.
     * 
     * @param alarmTime
     */
    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }


    /**
     * Gets the allDay value for this WsScheduleInfo.
     * 
     * @return allDay
     */
    public boolean isAllDay() {
        return allDay;
    }


    /**
     * Sets the allDay value for this WsScheduleInfo.
     * 
     * @param allDay
     */
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }


    /**
     * Gets the endDate value for this WsScheduleInfo.
     * 
     * @return endDate
     */
    public java.lang.String getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this WsScheduleInfo.
     * 
     * @param endDate
     */
    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the htmlContent value for this WsScheduleInfo.
     * 
     * @return htmlContent
     */
    public boolean isHtmlContent() {
        return htmlContent;
    }


    /**
     * Sets the htmlContent value for this WsScheduleInfo.
     * 
     * @param htmlContent
     */
    public void setHtmlContent(boolean htmlContent) {
        this.htmlContent = htmlContent;
    }


    /**
     * Gets the important value for this WsScheduleInfo.
     * 
     * @return important
     */
    public boolean isImportant() {
        return important;
    }


    /**
     * Sets the important value for this WsScheduleInfo.
     * 
     * @param important
     */
    public void setImportant(boolean important) {
        this.important = important;
    }


    /**
     * Gets the location value for this WsScheduleInfo.
     * 
     * @return location
     */
    public java.lang.String getLocation() {
        return location;
    }


    /**
     * Sets the location value for this WsScheduleInfo.
     * 
     * @param location
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }


    /**
     * Gets the mhtContent value for this WsScheduleInfo.
     * 
     * @return mhtContent
     */
    public boolean isMhtContent() {
        return mhtContent;
    }


    /**
     * Sets the mhtContent value for this WsScheduleInfo.
     * 
     * @param mhtContent
     */
    public void setMhtContent(boolean mhtContent) {
        this.mhtContent = mhtContent;
    }


    /**
     * Gets the senderEmail value for this WsScheduleInfo.
     * 
     * @return senderEmail
     */
    public java.lang.String getSenderEmail() {
        return senderEmail;
    }


    /**
     * Sets the senderEmail value for this WsScheduleInfo.
     * 
     * @param senderEmail
     */
    public void setSenderEmail(java.lang.String senderEmail) {
        this.senderEmail = senderEmail;
    }


    /**
     * Gets the startDate value for this WsScheduleInfo.
     * 
     * @return startDate
     */
    public java.lang.String getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this WsScheduleInfo.
     * 
     * @param startDate
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the subject value for this WsScheduleInfo.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this WsScheduleInfo.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsScheduleInfo)) return false;
        WsScheduleInfo other = (WsScheduleInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.alarmTime == other.getAlarmTime() &&
            this.allDay == other.isAllDay() &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            this.htmlContent == other.isHtmlContent() &&
            this.important == other.isImportant() &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation()))) &&
            this.mhtContent == other.isMhtContent() &&
            ((this.senderEmail==null && other.getSenderEmail()==null) || 
             (this.senderEmail!=null &&
              this.senderEmail.equals(other.getSenderEmail()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getAlarmTime();
        _hashCode += (isAllDay() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        _hashCode += (isHtmlContent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isImportant() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        _hashCode += (isMhtContent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getSenderEmail() != null) {
            _hashCode += getSenderEmail().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsScheduleInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "WsScheduleInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alarmTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "alarmTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allDay");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "allDay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("htmlContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "htmlContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("important");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "important"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mhtContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "mhtContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "senderEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.mail.ss.branch.neo.hanwha", "subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
