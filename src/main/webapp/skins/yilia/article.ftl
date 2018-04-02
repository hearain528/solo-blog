<#include "macro-head.ftl">
<#include "macro-comments.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${article.articleTitle} - ${blogTitle}">
        <meta name="keywords" content="${article.articleTags}" />
        <meta name="description" content="${article.articleAbstract?html}" />
        </@head>
        <#if previousArticlePermalink??>
            <link rel="prev" title="${previousArticleTitle}" href="${servePath}${previousArticlePermalink}">
        </#if>
        <#if nextArticlePermalink??>
            <link rel="next" title="${nextArticleTitle}" href="${servePath}${nextArticlePermalink}">
        </#if>
            <!-- Open Graph -->
            <meta property="og:locale" content="zh_CN"/>
            <meta property="og:type" content="article"/>
            <meta property="og:title" content="${article.articleTitle}"/>
            <meta property="og:description" content="${article.articleAbstract?html}"/>
            <meta property="og:image" content="${article.authorThumbnailURL}"/>
            <meta property="og:url" content="${servePath}${article.articlePermalink}"/>
            <meta property="og:site_name" content="Solo"/>
            <!-- Twitter Card -->
            <meta name="twitter:card" content="summary"/>
            <meta name="twitter:description" content="${article.articleAbstract?html}"/>
            <meta name="twitter:title" content="${article.articleTitle}"/>
            <meta name="twitter:image" content="${article.authorThumbnailURL}"/>
            <meta name="twitter:url" content="${servePath}${article.articlePermalink}"/>
            <meta name="twitter:site" content="@DL88250"/>
            <meta name="twitter:creator" content="@DL88250"/>
    </head>
    <body>
        <#include "side.ftl">
        <main>
            <article class="post article-body">
                <header>
                    <h2>
                        <a rel="bookmark" href="${servePath}${article.articlePermalink}">
                            ${article.articleTitle}
                        </a>
                        <#if article.articlePutTop>
                        <sup>
                            ${topArticleLabel}
                        </sup>
                        </#if>
                        <#if article.hasUpdated>
                        <sup>
                            ${updatedLabel}
                        </sup>
                        </#if>
                    </h2>
                    <time>
                        <span class="icon-date"></span> ${article.articleCreateDate?string("yyyy-MM-dd")}
                        &nbsp;&nbsp;&nbsp;<span class="icon-eye">阅读量</span>(${article.articleViewCount})
                        &nbsp;&nbsp;&nbsp;<span class="icon-bubble">评论量</span>(${article.articleCommentCount})
                    </time>

                    <section class="tags">
                        <span class="icon-tag"></span>  &nbsp;
                        <#list article.articleTags?split(",") as articleTag>
                        <a class="tag" rel="tag" href="${servePath}/tags/${articleTag?url('UTF-8')}">
                            ${articleTag}</a>
                        </#list>

                        <a rel="nofollow" href="${servePath}/authors/${article.authorId}">
                            <img class="avatar" title="${article.authorName}" alt="${article.authorName}" src="${article.authorThumbnailURL}"/>
                        </a>
                    </section>
                </header>
                <section class="abstract content-reset">
                    ${article.articleContent}
                    <#if "" != article.articleSign.signHTML?trim>
                    <div>
                        ${article.articleSign.signHTML}
                    </div>
                    </#if>

                    <#if nextArticlePermalink?? || previousArticlePermalink??>
                    <aside class="fn-clear">
                        <#if previousArticlePermalink??>
                        <a class="fn-left" rel="prev" href="${servePath}${previousArticlePermalink}">
                            <strong>&lt;</strong> ${previousArticleTitle}
                        </a>
                        </#if>
                        <#if nextArticlePermalink??>
                        <a class="fn-right" rel="next" href="${servePath}${nextArticlePermalink}">
                            ${nextArticleTitle} <strong>&gt;</strong>
                        </a>
                        </#if>
                    </aside>
                    </#if>
                </section>

                <div id="externalRelevantArticles" class="abstract"></div>
            </article>
            <@comments commentList=articleComments article=article></@comments>

            <#include "footer.ftl">

            <@comment_script oId=article.oId>
            page.tips.externalRelevantArticlesDisplayCount = "${externalRelevantArticlesDisplayCount}";
            <#if 0 != externalRelevantArticlesDisplayCount>
            page.loadExternalRelevantArticles("<#list article.articleTags?split(",") as articleTag>${articleTag}<#if articleTag_has_next>,</#if></#list>");
            </#if>
            </@comment_script>    
        </main>
    </body>
</html>
