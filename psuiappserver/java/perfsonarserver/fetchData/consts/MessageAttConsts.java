package perfsonarserver.fetchData.consts;

/**
 * Contains constants which are used to access/generate attributes in soap
 * messages.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class MessageAttConsts
{
	public static final String MINUS_TWO = "-2";
	public static final String MINUS_THREE = "-3" ;
	public static final String CONSOLIDATION_FUNCTION = "consolidationFunction";
	public static final String DATA_SOURCE_HEARTBEAT = "dataSourceHeartbeat";
	public static final String DATA_SOURCE_MAX_VALUE = "dataSourceMaxValue";
	public static final String DATA_SOURCE_MIN_VALUE = "dataSourceMinValue";
	public static final String DATA_SOURCE_STEP = "dataSourceStep";
	public static final String DATA_SOURCE_TYPE = "dataSourceType";
	public static final String DST = "dst";
	public static final String DUPLICATES = "duplicates";
	public static final String END_TIME = "endTime";
	public static final String EVENT_TYPE = "eventType";
	public static final String GROUPSIZE = "groupsize";
	public static final String ID = "id";
	public static final String IF_NAME = "IFName";
	public static final String INTERVAL = "interval";
	public static final String LOSS = "loss";
	public static final String MAX_DELAY = "max_delay";
	public static final String MAX_IPDV_JITTER = "max_ipdv_jitter";
	public static final String MED_DELAY = "med_delay";
	public static final String MED_IPDV_JITTER = "med_ipdv_jitter";
	public static final String METADATA_ID_REF = "metadataIdRef";
	public static final String METADATA_KEY_REQUEST = "MetadataKeyRequest";
	public static final String MID = "mid";
	public static final String MIN_DELAY = "min_delay";
	public static final String MIN_IPDV_JITTER = "min_ipdv_jitter";
	public static final String NAME = "name";
	public static final String NAN = "nan";
	public static final String NUM_BYTES_UNITS = "numBytesUnits";
	public static final String PACKETSIZE = "packetsize";
	public static final String PARAM = "param";
	public static final String PARAMETER = "parameter";
	public static final String PARAMETERS = "parameters";
	public static final String PRECEDENCE = "precedence";
	public static final String PROTOCOL = "protocol";
	public static final String DESTINATION = "receiver";
	public static final String DESTINATION_IP = "receiver_ip";
	public static final String RESOLUTION = "resolution";
	public static final String RESULT = "result";
	public static final String SOURCE = "sender";
	public static final String SOURCE_IP = "sender_ip";
	public static final String SETUP_DATA_REQUEST = "SetupDataRequest";
	public static final String SRC = "src";
	public static final String START_TIME = "startTime";
	public static final String SYNC = "sync";
	public static final String TIME = "time";
	public static final String TIME_VALUE = "timeValue";
	public static final String TYPE = "type";
	public static final int UNIX_FACTOR = 1000;
	public static final String VALUE = "value";
	public static final String VALUE_UNITS = "valueUnits";
	public static final String ID_CHAR = ":";

	private MessageAttConsts()
	{
	}
}
