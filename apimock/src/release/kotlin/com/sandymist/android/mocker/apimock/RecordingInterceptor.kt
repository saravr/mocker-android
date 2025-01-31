import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RecordingInterceptor(@Suppress("UNUSED_PARAMETER") context: Context): Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
