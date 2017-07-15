from django.conf.urls import include, url
from django.contrib import admin
from marina.views import MainView


urlpatterns = [
    url(r'^admin', include(admin.site.urls)),
    url(r'^', MainView.as_view())
]
