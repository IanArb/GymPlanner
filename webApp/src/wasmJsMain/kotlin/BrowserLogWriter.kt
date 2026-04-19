import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

class BrowserLogWriter : LogWriter() {
    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val formatted = "[$tag] $message"
        when (severity) {
            Severity.Verbose,
            Severity.Debug -> consoleLog(formatted)
            Severity.Info -> consoleInfo(formatted)
            Severity.Warn -> consoleWarn(formatted)
            Severity.Error,
            Severity.Assert -> consoleError(formatted)
        }
        throwable?.let { consoleError(it.stackTraceToString()) }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(msg) => console.log(msg)")
private external fun consoleLog(msg: String)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(msg) => console.info(msg)")
private external fun consoleInfo(msg: String)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(msg) => console.warn(msg)")
private external fun consoleWarn(msg: String)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(msg) => console.error(msg)")
private external fun consoleError(msg: String)
