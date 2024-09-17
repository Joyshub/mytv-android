package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsCategoryScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screensold.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme

@Composable
fun SettingsEpgScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(),
    toEpgSourceScreen: () -> Unit = {},
    toEpgRefreshTimeThresholdScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    SettingsCategoryScreen(
        modifier = modifier,
        header = { Text("设置 / 节目单") },
        onBackPressed = onBackPressed,
    ) { firstItemFocusRequester ->
        item {
            SettingsListItem(
                modifier = Modifier.focusRequester(firstItemFocusRequester),
                headlineContent = "节目单启用",
                supportingContent = "首次加载时可能会较为缓慢",
                trailingContent = { Switch(settingsViewModel.epgEnable, null) },
                onSelect = { settingsViewModel.epgEnable = !settingsViewModel.epgEnable },
            )
        }

        item {
            val currentEpgSource = settingsViewModel.epgSourceCurrent

            SettingsListItem(
                headlineContent = "自定义节目单",
                trailingContent = { Text(currentEpgSource.name) },
                onSelect = toEpgSourceScreen,
                link = true,
            )
        }

        item {
            val epgRefreshTimeThreshold = settingsViewModel.epgRefreshTimeThreshold

            SettingsListItem(
                headlineContent = "节目单刷新时间阈值",
                trailingContent = { Text("${epgRefreshTimeThreshold}:00") },
                supportingContent = "时间不到${epgRefreshTimeThreshold}:00节目单将不会刷新",
                onSelect = toEpgRefreshTimeThresholdScreen,
                link = true,
            )
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsEpgScreenPreview() {
    MyTvTheme {
        SettingsEpgScreen()
    }
}