package top.yogiczy.mytv.tv.ui.screen.settings.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import top.yogiczy.mytv.core.data.entities.channel.ChannelGroupList
import top.yogiczy.mytv.core.data.utils.ChannelAlias
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.material.Tag
import top.yogiczy.mytv.tv.ui.material.TagDefaults
import top.yogiczy.mytv.tv.ui.rememberChildPadding
import top.yogiczy.mytv.tv.ui.screen.components.AppScreen
import top.yogiczy.mytv.tv.ui.screen.settings.components.SettingsListItem
import top.yogiczy.mytv.tv.ui.screensold.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.theme.MyTvTheme
import top.yogiczy.mytv.tv.ui.utils.Configs

@Composable
fun SettingsIptvScreen(
    modifier: Modifier = Modifier,
    channelGroupListProvider: () -> ChannelGroupList = { ChannelGroupList() },
    settingsViewModel: SettingsViewModel = viewModel(),
    toIptvSourceScreen: () -> Unit = {},
    toIptvSourceCacheTimeScreen: () -> Unit = {},
    toChannelGroupVisibilityScreen: () -> Unit = {},
    toIptvHybridModeScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val childPadding = rememberChildPadding()

    AppScreen(
        modifier = modifier.padding(top = 10.dp),
        header = { Text("设置 / 直播源") },
        canBack = true,
        onBackPressed = onBackPressed,
    ) {
        LazyColumn(
            contentPadding = childPadding.copy(top = 10.dp).paddingValues,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                val currentIptvSource = settingsViewModel.iptvSourceCurrent

                SettingsListItem(
                    headlineContent = "自定义直播源",
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Tag(
                                if (currentIptvSource.isLocal) "本地" else "远程",
                                colors = TagDefaults.colors(
                                    containerColor = LocalContentColor.current.copy(0.1f)
                                ),
                            )
                            Text(currentIptvSource.name)
                        }
                    },
                    onSelected = toIptvSourceScreen,
                    link = true,
                )
            }

            item {
                val cacheTime = settingsViewModel.iptvSourceCacheTime

                SettingsListItem(
                    headlineContent = "直播源缓存时间",
                    trailingContent = when (cacheTime) {
                        0L -> "不缓存"
                        Long.MAX_VALUE -> "永久"
                        else -> cacheTime.humanizeMs()
                    },
                    onSelected = toIptvSourceCacheTimeScreen,
                    link = true,
                )
            }

            item {
                val allCount = channelGroupListProvider().size
                val hiddenCount = settingsViewModel.iptvChannelGroupHiddenList.size

                SettingsListItem(
                    headlineContent = "频道分组管理",
                    trailingContent = {
                        if (hiddenCount == 0) {
                            Text("共${allCount}个分组")
                        } else {
                            Text("共${allCount}个分组，已隐藏${hiddenCount}个分组")
                        }
                    },
                    onSelected = toChannelGroupVisibilityScreen,
                    link = true,
                )
            }

            item {
                val alias = ChannelAlias.aliasMap

                SettingsListItem(
                    headlineContent = "频道别名",
                    trailingContent = {
                        Text("共${alias.size}个频道，${alias.values.sumOf { it.size }}个别名")
                    },
                    remoteConfig = true,
                )
            }

            item {
                SettingsListItem(
                    headlineContent = "相似频道合并",
                    supportingContent = "相同频道别名将进行合并",
                    trailingContent = {
                        Switch(settingsViewModel.iptvSimilarChannelMerge, null)
                    },
                    onSelected = {
                        settingsViewModel.iptvSimilarChannelMerge =
                            !settingsViewModel.iptvSimilarChannelMerge
                    },
                )
            }

            item {
                val hybridMode = settingsViewModel.iptvHybridMode

                SettingsListItem(
                    headlineContent = "混合模式",
                    trailingContent = {
                        Text(
                            when (hybridMode) {
                                Configs.IptvHybridMode.DISABLE -> "禁用"
                                Configs.IptvHybridMode.IPTV_FIRST -> "直播源优先"
                                Configs.IptvHybridMode.HYBRID_FIRST -> "混合优先"
                            }
                        )
                    },
                    onSelected = toIptvHybridModeScreen,
                    link = true,
                )
            }
        }
    }
}

@Preview(device = "id:Android TV (720p)")
@Composable
private fun SettingsIptvScreenPreview() {
    MyTvTheme {
        SettingsIptvScreen()
    }
}