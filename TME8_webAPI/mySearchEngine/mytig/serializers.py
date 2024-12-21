from rest_framework.serializers import ModelSerializer
from mytig.models import ProduitEnPromotion

class ProduitEnPromotionSerializer(ModelSerializer):
    class Meta:
        model = ProduitEnPromotion
        fields = ('id', 'tigID')
