package ara.util;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import peersim.config.Configuration;

/**
 * @author jonathan.lejeune@lip6.fr
 *
 */
public final class Constantes {

	/* Ne peut être ni instanciée ni étendue */
	private Constantes() {
	}

	// Logger pour le debug : niveau INFO = affiche les sections critiques, niveau
	// FINE = affiche les évenements
	public static final java.util.logging.Logger log = java.util.logging.Logger.getLogger("log");
	private static final String PAR_LOG_LEVEL = "loglevel";// niveau INFO par defaut

	static {

		Handler handlerObj = new ConsoleHandler();
		log.setUseParentHandlers(false);
		handlerObj.setLevel(Level.ALL);
		handlerObj.setFormatter(new Formatter() {

			@Override
			public String format(LogRecord record) {
				String format = "%4$s : %5$s  (%2$s)%n";// initial was "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp
														// %2$s%n%4$s: %5$s%6$s%n"
				String source;
				if (record.getSourceClassName() != null) {
					source = record.getSourceClassName();
					String tmp[] = source.split("\\.");
					source = tmp[tmp.length - 1];
					if (record.getSourceMethodName() != null) {
						source += " " + record.getSourceMethodName();
					}
				} else {
					source = record.getLoggerName();
				}
				String message = formatMessage(record);
				String throwable = "";
				return String.format(format, new Date(record.getMillis()), source, record.getLoggerName(),
						record.getLevel(), message, throwable);
			}
		});

		log.addHandler(handlerObj);
		log.setLevel(Level.parse(Configuration.getString(PAR_LOG_LEVEL, Level.INFO.getName())));
	}

}
