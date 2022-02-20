package nz.co.jonker.motors.ui

import android.view.View
import io.mockk.mockk
import io.mockk.verify
import nz.co.jonker.motors.ui.SearchViewModel.ScreenState
import org.junit.Test

class ScreenStateTest {

    @Test
    fun `Given Loading When setProgressVisibility Then show progress bar`() {
        val mockProgressBar: View = mockk(relaxed = true)

        ScreenState.Loading.setProgressVisibility(mockProgressBar)

        verify { mockProgressBar.visibility = View.VISIBLE}
    }

    @Test
    fun `Given Error When setProgressVisibility Then hide progress bar`() {
        val mockProgressBar: View = mockk(relaxed = true)

        ScreenState.Error("").setProgressVisibility(mockProgressBar)

        verify { mockProgressBar.visibility = View.GONE}
    }

    @Test
    fun `Given Good When setProgressVisibility Then hide progress bar`() {
        val mockProgressBar: View = mockk(relaxed = true)

        ScreenState.Good.setProgressVisibility(mockProgressBar)

        verify { mockProgressBar.visibility = View.GONE}
    }
}
