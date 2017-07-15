from django.conf.urls import include, url
from django.contrib import admin
from marina.views import MainView, BoatViewSet, SlipViewSet
from rest_framework.routers import DefaultRouter


router = DefaultRouter()
router.register(r'boats', BoatViewSet, base_name="boats")
router.register(r'slips', SlipViewSet, base_name="slips")


urlpatterns = [
    url(r'^admin', include(admin.site.urls)),
    url(r'^$', MainView.as_view()),
    url(r'^api/', include(router.urls)),
]
